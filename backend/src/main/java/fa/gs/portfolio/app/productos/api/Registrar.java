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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController("productos.registrar")
@RequestMapping("/")
public class Registrar {

    private AppLogger log = AppLogger.get(Registrar.class);

    @Autowired
    private ProductosService productos;

    /**
     * Permite crear un nuevo registro.
     *
     * @param input Cuerpo de peticion.
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @PostMapping(
            path = "/v1/productos",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume(@RequestBody Registrar.Input input) throws Exception {
        // Control.
        if (input.getNombre() == null || input.getNombre().isEmpty()) {
            throw new IllegalArgumentException("Se debe especificar un nombre de producto.");
        }

        // Registrar producto.
        Integer id = productos.registrar(input.nombre, input.descripcion, input.categoria);

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
