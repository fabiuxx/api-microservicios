from flask import Flask, request
from flask_cors import CORS
import requests
import json
import os
import sys
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.resources import SERVICE_NAME, Resource
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.trace.propagation.tracecontext import TraceContextTextMapPropagator

# Variable de entorno con clave de ingestion para signoz.
OTLP_INGEST_KEY = ""
if "OTLP_INGEST_KEY" in os.environ.keys():
    OTLP_INGEST_KEY = os.environ["OTLP_INGEST_KEY"]

# Control de variable de entorno.
if OTLP_INGEST_KEY == "":
    print("Se esperaba la variable de entorno 'OTLP_INGEST_KEY'.")
    sys.exit(1)

# Componentes propios de Flask.
app = Flask(__name__)
cors = CORS(app)

# Inicializacion de OTLP.
resource = Resource.create({SERVICE_NAME: 'api-gateway' })
provider = TracerProvider(resource=resource)
processor = BatchSpanProcessor(OTLPSpanExporter(endpoint="https://ingest.us.signoz.cloud:443", headers="signoz-access-token={0}".format(OTLP_INGEST_KEY)))
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)
tracer = trace.get_tracer("my.tracer.name")

'''
Endpoints de servicio disponibles.
'''
ENDPOINTS = dict()

'''
Obtiene la lista de endpoints registrados en el servicio de registro/descubrimiento.
'''
def load_available_endpoints():
    with tracer.start_as_current_span("load_available_endpoints") as span:
        # Contexto para OTLP.
        carrier = dict()
        TraceContextTextMapPropagator().inject(carrier)
        
        try:
            # Solicitar lista de endpoints.
            response = requests.get("http://127.0.0.1:9000/list", headers={
                'X-Trace': carrier['traceparent']
            })
            
            # Agregar lista de endpoints.
            endpoints = response.json()
            for endpoint in endpoints:
                ENDPOINTS[endpoint["nombre"]] = endpoint
            
            span.set_status(trace.StatusCode.OK)    
        except Exception as ex:
            span.set_status(trace.StatusCode.ERROR)
            span.record_exception(ex)

def build_endpoint_url(endpoint_name):
    if endpoint_name in ENDPOINTS.keys():
        endpoint = ENDPOINTS[endpoint_name]
        txt_a = '{0}:{1}/{2}'.format(endpoint['host'], endpoint['port'], endpoint['basePath'])
        txt_b = txt_a.replace("//", "/")
        while txt_a != txt_b:
            txt_a = txt_b
            txt_b = txt_b.replace("//", "/")
        return '{0}://{1}'.format(endpoint['scheme'], txt_b)
    return None

def consume_registrar_producto(nombre, descripcion, categoria):
    with tracer.start_as_current_span("consume_endpoint") as span:
        # Recargar endpoints.
        load_available_endpoints();
        
        try:
            # Obtener url.
            url = build_endpoint_url("registrar_producto")
            if url is None:
                raise Exception("Sin url para servicio: productos")
            
            # Control.
            span.set_attribute("attr.endpoint", "registrar_producto")
            span.set_attribute("attr.endpoint.url", url)
        
            # Body.
            body = json.dumps({
                "nombre": nombre,
                "descripcion": descripcion,
                "categoria": categoria
            })
            span.set_attribute("attr.endpoint.data", body)
        
            # Realizar peticion.
            response = requests.post(
                url, 
                headers={
                    "Content-Type": "application/json"
                }, 
                data= body
            )
            
            # Control.
            id_producto = None
            json_response = response.json()
            if json_response["status"]["success"] == True:
                id_producto = int(json_response["payload"])
            
            span.set_status(trace.StatusCode.OK)
            return id_producto
        except Exception as ex:
            span.set_status(trace.StatusCode.ERROR)
            span.record_exception(ex)
            
def consume_registrar_stock(id_producto, precio, stock):
    with tracer.start_as_current_span("consume_endpoint") as span:
        # Recargar endpoints.
        load_available_endpoints();
        
        try:
            # Obtener url.
            url = build_endpoint_url("registrar_stock")
            if url is None:
                raise Exception("Sin url para servicio: stock")
            
            # Control.
            span.set_attribute("attr.endpoint", "registrar_stock")
            span.set_attribute("attr.endpoint.url", url)
        
            # Body
            body = json.dumps({
                "id_producto": int(id_producto),
                "precio": int(precio),
                "stock": int(stock)
            })
            span.set_attribute("attr.endpint.data", body)
        
            # Realizar peticion.
            response = requests.post(
                url, 
                headers={
                    "Content-Type": "application/json"
                }, 
                data= body
            )
            
            # Control.
            json_response = response.json()
            ok = (json_response["status"]["success"] == True)
            
            span.set_status(trace.StatusCode.OK)
            return ok
        except Exception as ex:
            span.set_status(trace.StatusCode.ERROR)
            span.record_exception(ex)

'''
Permite verificar si el servicio esta activo.
'''
@app.route("/ping", methods=["GET"])
def ping():
    with tracer.start_as_current_span("api/ping") as span:
        span.set_status(trace.StatusCode.OK)
        return ("pong", 200)
    
'''
Permite dar de alta a un nuevo producto, junto a su stock.
'''
@app.route("/producto", methods=["POST"])
def alta_producto():
    with tracer.start_as_current_span("api/producto/alta") as span:
        try:
            with tracer.start_as_current_span("api/producto/alta/read_params"):
                # Datos de producto.
                p = request.json["producto"]
                p_nombre = p["nombre"]
                p_descripcion = p["descripcion"] if "descripcion" in p.keys() else ""
                p_categoria = p["categoria"] if "categoria" in p.keys() else ""
                
                # Datos de stock.
                s = request.json["stock"]
                s_precio = s["precio"]
                s_stock = s["stock"]
            
                # Consumir servicio de registro de producto.
                id_producto = consume_registrar_producto(p_nombre, p_descripcion, p_categoria)
                if id_producto is None:
                    raise Exception("No se pudo registrar el producto.")
                
                # Consumir servicio de registro de stock.
                ok = consume_registrar_stock(id_producto, s_precio, s_stock)
                if ok == False:
                    raise Exception("No se pudo registrar el stock.")
            
            span.set_status(trace.StatusCode.OK)
            return ("ok", 200)
        except Exception as ex:
            span.set_status(trace.StatusCode.ERROR)
            span.record_exception(ex)
            return ("ko", 200)

# Punto de entrada de programa.
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=9001, debug=True)
    provider.shutdown()