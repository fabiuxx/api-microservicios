/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.api.ApiResponses;
import fa.gs.portfolio.app.stock.StockService;
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
@RestController("stock.registrar")
@RequestMapping("/")
public class Registrar {

    private AppLogger log = AppLogger.get(Registrar.class);

    @Autowired
    private StockService stocks;

    /**
     * Permite crear un nuevo registro.
     *
     * @param input Cuerpo de peticion.
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @PostMapping(
            path = "/v1/stock",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume(@RequestBody Registrar.Input input) throws Exception {
        // Control.
        if (input.getIdProducto() == null) {
            throw new IllegalArgumentException("Se debe especificar un identificador de producto.");
        }

        // Registrar stock de producto.
        Integer id = stocks.registrar(input.idProducto, input.precio, input.stock);

        // Ok.
        JsonElement json = ApiResponses.ok(new JsonPrimitive(id));
        return ResponseEntity.ok(json);
    }

    @Data
    public static class Input implements Serializable {

        @SerializedName(value = "id_producto", alternate = "idProducto")
        private Integer idProducto;
        private Integer precio;
        private Integer stock;
    }

}
