/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.api.ApiResponses;
import fa.gs.portfolio.app.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController("stock.eliminar")
@RequestMapping("/")
public class Eliminar {

    private AppLogger log = AppLogger.get(Eliminar.class);

    @Autowired
    private StockService stocks;

    /**
     * Permite eliminar un registro dado.
     *
     * @param id Identificador de stock de producto.
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @DeleteMapping(
            path = "/v1/stock/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume(@PathVariable String id) throws Exception {
        // Control.
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Se debe especificar un identificador de stock de producto.");
        }

        // Control.
        Integer iId;
        try {
            iId = Integer.valueOf(id);
        } catch (Throwable thr) {
            throw new IllegalArgumentException("Se debe especificar un identificador de stock de producto. Debe ser un valor numerico.");
        }

        // Modificar stock de producto.
        stocks.eliminar(iId);

        // Ok.
        JsonElement json = ApiResponses.ok(new JsonPrimitive(id));
        return ResponseEntity.ok(json);
    }

}
