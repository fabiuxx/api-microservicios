from functools import wraps
import json
import os
import sys
from flask import Flask, request
from flask_cors import CORS
from opentelemetry import trace, context
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
resource = Resource.create({SERVICE_NAME: 'services-registry' })
provider = TracerProvider(resource=resource)
processor = BatchSpanProcessor(OTLPSpanExporter(endpoint="https://ingest.us.signoz.cloud:443", headers="signoz-access-token={0}".format(OTLP_INGEST_KEY)))
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)
tracer = trace.get_tracer("my.tracer.name")

'''
Decorador que permite "unirnos" a una traza activa, siempre y cuando la cabecera HTTP correspodiente
este presente.
'''
def otlp_context_propagation_support(func):
    @wraps(func)
    def wrapper_func(*args, **kwargs):
        value = request.headers["X-Trace"] if "X-Trace" in request.headers.keys() else ""
        if value != "":
            ctx = TraceContextTextMapPropagator().extract({'traceparent': value})
            token = context.attach(ctx)
            try:
                return func(*args, **kwargs)
            finally:
                context.detach(token)
        else:
            return func(*args, **kwargs)
    return wrapper_func

'''
Encapsula la informacion de definicion para un endpoint de servicio. Inicialmente, y
para mantener la implementacion simple, solo se consideran servicios rest.
'''
class ServiceEndpoint():
    def __init__(self, nombre, scheme, host, port, basePath):
        self.nombre = nombre
        self.scheme = scheme
        self.host = host
        self.port = port
        self.basePath = basePath
        
'''
Cache para endpoints registrados.
'''
ENDPOINTS = dict()

'''
Permite verificar si el servicio esta activo.
'''
@app.route("/ping", methods=["GET"])
@otlp_context_propagation_support
def ping():
    with tracer.start_as_current_span("api/ping") as span:
        span.set_status(trace.StatusCode.OK)
        return ("pong", 200)

'''
Permite listar los endpoints disponibles.
'''
@app.route("/list", methods=["GET"])
@otlp_context_propagation_support
def listar():
    with tracer.start_as_current_span("api/list") as span:
        elements = list()
        for nombre in ENDPOINTS.keys():
            endpoint = ENDPOINTS[nombre]
            element = dict()
            element["nombre"] = endpoint.nombre
            element["scheme"] = endpoint.scheme
            element["host"] = endpoint.host
            element["port"] = endpoint.port
            element["basePath"] = endpoint.basePath
            elements.append(element)
        span.set_attribute("attr.count", len(elements))
        span.set_status(trace.StatusCode.OK)
        
    return app.response_class(
        response=json.dumps(elements),
        status=200,
        mimetype='application/json'
    )

'''
Permite registrar un nuevo endpoint disponible.
'''
@app.route("/register", methods=["POST"])
@otlp_context_propagation_support
def register():
    with tracer.start_as_current_span("api/register") as span:
        try:
            with tracer.start_as_current_span("api/register/read_params"):
                # Datos de endpoint
                nombre = request.json["nombre"]
                scheme = request.json["scheme"]
                host = request.json["host"]
                port = request.json["port"]
                basePath = request.json["basePath"]
            
                with tracer.start_as_current_span("api/register/write") as span0:
                    # Registrar
                    span0.set_attribute("attr.servicio", nombre)
                    endpoint = ServiceEndpoint(nombre, scheme, host, port, basePath)
                    ENDPOINTS[nombre] = endpoint
            
            span.set_status(trace.StatusCode.OK)
            return ("ok", 200)
        except Exception as ex:
            span.set_status(trace.StatusCode.ERROR)
            span.record_exception(ex)
            return ("ko", 200)

# Punto de entrada de programa.
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=9000, debug=True)
    provider.shutdown()