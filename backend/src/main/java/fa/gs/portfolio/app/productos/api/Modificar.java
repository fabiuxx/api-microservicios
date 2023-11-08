/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.productos.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.api.ApiResponses;
import fa.gs.portfolio.app.productos.ProductosService;
import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController
public class Modificar {

    private AppLogger log = AppLogger.get(Modificar.class);

    @Autowired
    private ProductosService productos;

    /**
     * Permite modificar un registro dado.
     *
     * @param id Identificador de producto.
     * @param input Cuerpo de peticion.
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @PutMapping(
            path = "/v1/productos/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume(@PathVariable String id, @RequestBody Modificar.Input input) throws Exception {
        // Control.
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Se debe especificar un identificador de producto.");
        }

        // Control.
        Integer iId;
        try {
            iId = Integer.valueOf(id);
        } catch (Throwable thr) {
            throw new IllegalArgumentException("Se debe especificar un identificador de producto. Debe ser un valor numerico.");
        }

        // Control.
        if (input.getNombre() == null || input.getNombre().isEmpty()) {
            throw new IllegalArgumentException("Se debe especificar un nombre de producto.");
        }

        // Modificar producto.
        productos.modificar(iId, input.nombre, input.descripcion, input.categoria);

        // Ok.
        JsonElement json = ApiResponses.ok(new JsonPrimitive(id));
        return ResponseEntity.ok(json);
    }

    @Data
    public static class Input implements Serializable {

        private String nombre;
        private String descripcion;
        private String categoria;
    }

}
