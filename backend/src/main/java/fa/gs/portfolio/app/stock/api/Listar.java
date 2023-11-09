/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.api.ApiResponses;
import fa.gs.portfolio.app.stock.StockService;
import fa.gs.portfolio.app.stock.entities.Stock;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController("stock.listar")
@RequestMapping("/")
public class Listar {

    private AppLogger log = AppLogger.get(Listar.class);

    @Autowired
    private StockService stocks;

    /**
     * Permite obtener todos los registros existentes.
     *
     * @return Siempre retorna {@code pong}.
     * @throws java.lang.Exception Si no es posible realizar la operacion.
     */
    @GetMapping(
            path = "/v1/stock",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<JsonElement> consume() throws Exception {
        Collection<Stock> elementos = stocks.listar();

        JsonArray array = new JsonArray();
        elementos.stream().map(this::adapt).forEach(array::add);

        JsonObject json = ApiResponses.ok(array);
        return ResponseEntity.ok(json);
    }

    private JsonElement adapt(Stock stock) {
        JsonObject instance = new JsonObject();
        instance.add("id", new JsonPrimitive(stock.getId()));
        instance.add("id_producto", new JsonPrimitive(stock.getIdProducto()));
        instance.add("precio", new JsonPrimitive(stock.getPrecio()));
        instance.add("stock", new JsonPrimitive(stock.getStock()));
        return instance;
    }

}
