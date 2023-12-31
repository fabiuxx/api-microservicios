#!/bin/sh

# Inicializar servicio de registro.
function setup_registry_service() {
    cd /home/$USER/registry
    nohup python server.py >/dev/null 2>&1 &
}

# Inicializar servicio de gateway.
function setup_gateway_service() {
    cd /home/$USER/gateway
    nohup python server.py >/dev/null 2>&1 &
}

# Permite esperar a que un servicio este listo para ser utilizado.
function wait_microservice_is_up() {
    while true; do
        test "$(wget -qO- http://127.0.0.1:$1/v1/ping 2>/dev/null)" = "pong"
        if [ $? -eq 0 ]; then
            break
        fi
        sleep 1s
    done
}

# Inicializar servicio de productos.
function setup_productos_microservice() {
    cd /home/$USER/backend
    export OTEL_SERVICE_NAME=productos
    nohup java -server -javaagent:opentelemetry-javaagent.jar -jar api-microservices-0.0.1.jar --server.port=8000 --app.name=productos >/dev/null 2>&1 &

    # Esperar a que este listo.
    echo "iniciando ..."
    wait_microservice_is_up 8000
}

# Inicializar servicio de stock.
function setup_stock_microservice() {
    cd /home/$USER/backend
    export OTEL_SERVICE_NAME=stock
    nohup java -server -javaagent:opentelemetry-javaagent.jar -jar api-microservices-0.0.1.jar --server.port=8001 --app.name=stock >/dev/null 2>&1 &

    # Esperar a que este listo.
    echo "iniciando ..."
    wait_microservice_is_up 8001
}

echo "preparando servicio de registro/descubrimiento de servicios ..."
setup_registry_service

export OTEL_PROPAGATORS=tracecontext
export OTEL_TRACES_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_ENDPOINT=https://ingest.us.signoz.cloud:443
export OTEL_EXPORTER_OTLP_HEADERS=signoz-access-token=$OTLP_INGEST_KEY

echo "preparando microservicio de productos ..."
setup_productos_microservice

echo "preparando microservicio de stock ..."
setup_stock_microservice

echo "preparando servicio de gateway ..."
setup_gateway_service

echo "listo."
tail -f /dev/null