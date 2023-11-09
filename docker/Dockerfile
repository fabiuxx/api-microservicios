FROM alpine:latest as base

ENV GROUP=appgroup
ENV USER=appuser

# Instalar paquetes necesarios.
RUN apk add unzip
RUN apk add xz
RUN apk add tar
RUN apk add python3
RUN apk add py3-pip
RUN apk add openjdk8

# Crear grupo y usuario por defecto.
RUN addgroup -S $GROUP
RUN adduser -S $USER -G $GROUP
USER $USER

# Preparar servicio de registro.
ENV OTLP_INGEST_KEY=
WORKDIR /home/$USER
COPY registry /home/$USER/registry
RUN pip install -r /home/$USER/registry/requirements.txt

# Preparar servicio de backend multi-servicios.
WORKDIR /home/$USER
RUN mkdir backend
COPY backend/target/lib /home/$USER/backend/lib
COPY backend/target/api-microservices-0.0.1.jar /home/$USER/backend/api-microservices-0.0.1.jar

COPY ./docker/entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 9000
EXPOSE 9001
EXPOSE 8000
EXPOSE 8001