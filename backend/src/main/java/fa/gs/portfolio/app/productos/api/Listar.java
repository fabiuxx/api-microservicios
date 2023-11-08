/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.productos.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.api.ApiResponses;
import fa.gs.portfolio.app.productos.ProductosService;
import fa.gs.portfolio.app.productos.entities.Producto;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController
public class Listar {

    private AppLogger log = AppLogger.get(Listar.class);

    @Autowired
    private ProductosService productos;

    /**
     * Permite obtener todos los registros existentes.
     *
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @GetMapping(
            path = "/v1/productos",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume() throws Exception {
        Collection<Producto> elementos = productos.listar();

        JsonArray array = new JsonArray();
        elementos.stream().map(this::adapt).forEach(array::add);

        JsonObject json = ApiResponses.ok(array);
        return ResponseEntity.ok(json);
    }

    private JsonElement adapt(Producto producto) {
        JsonObject instance = new JsonObject();
        instance.add("id", new JsonPrimitive(producto.getId()));
        instance.add("nombre", new JsonPrimitive(producto.getNombre()));
        instance.add("descripcion", new JsonPrimitive(producto.getDescripcion()));
        instance.add("categoria", new JsonPrimitive(producto.getCategoria()));
        return instance;
    }

}
