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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController
public class Eliminar {

    private AppLogger log = AppLogger.get(Eliminar.class);

    @Autowired
    private ProductosService productos;

    /**
     * Permite eliminar un registro dado.
     *
     * @param id Identificador de producto.
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @DeleteMapping(
            path = "/v1/productos/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume(@PathVariable String id) throws Exception {
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

        // Modificar producto.
        productos.eliminar(iId);

        // Ok.
        JsonElement json = ApiResponses.ok(new JsonPrimitive(id));
        return ResponseEntity.ok(json);
    }

}
