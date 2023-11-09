/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fa.gs.portfolio.app.base.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fa.gs.portfolio.AppLogger;
import java.util.Objects;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Permite exportar los endpoints del API REST implementado a algun servicio de
 * registro/descubrimiento.
 *
 * @author Fabio A. González Sosa
 */
public interface ApiEndpointsExporter {

    /**
     * Punto para proceso de exportacion.
     *
     * @throws Throwable Si ocurre algun error.
     */
    void export() throws Throwable;

    /**
     * Implmentacion simple de registro de endpoint.
     *
     * @param nombre Nombre de enpoint.
     * @param scheme Esquema.
     * @param host Host.
     * @param port Puerto.
     * @param basePath Path base.
     * @return {@code true} si el registro fue exitoso, caso contrario
     * {@code false}.
     */
    public static boolean simple(String nombre, String scheme, String host, String port, String basePath) {
        AppLogger log = AppLogger.get(ApiEndpointsExporter.class);
        try {
            // Cuerpo para registro de endpoint.
            JsonObject json = new JsonObject();
            json.add("nombre", new JsonPrimitive(nombre));
            json.add("scheme", new JsonPrimitive(scheme));
            json.add("host", new JsonPrimitive(host));
            json.add("port", new JsonPrimitive(port));
            json.add("basePath", new JsonPrimitive(basePath));

            // Cabeceras.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Envio de peticion.
            HttpEntity<String> body = new HttpEntity<>(json.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:9000/register", body, String.class);

            // Ok.
            String status = response.getBody();
            return Objects.equals(status, "ok");
        } catch (Throwable thr) {
            log.warning(thr, "No se pudo registrar endpoint: %s", nombre);
            return false;
        }
    }

}
