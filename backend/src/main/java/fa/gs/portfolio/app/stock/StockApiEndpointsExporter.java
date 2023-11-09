/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock;

import fa.gs.portfolio.AppParameters;
import fa.gs.portfolio.app.base.api.ApiEndpointsExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fabio A. González Sosa
 */
@Component
public class StockApiEndpointsExporter implements ApiEndpointsExporter {

    @Autowired
    private AppParameters params;

    /**
     * {@inheritDoc }
     */
    @Override
    public void export() throws Throwable {
        ApiEndpointsExporter.simple("listar_stock", "http", "127.0.0.1", params.getAppPort(), "/v1/stock");
        ApiEndpointsExporter.simple("registrar_stock", "http", "127.0.0.1", params.getAppPort(), "/v1/stock");
        ApiEndpointsExporter.simple("modificar_stock", "http", "127.0.0.1", params.getAppPort(), "/v1/stock");
        ApiEndpointsExporter.simple("eliminar_stock", "http", "127.0.0.1", params.getAppPort(), "/v1/stock");
    }

}
