/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.productos;

import fa.gs.portfolio.AppParameters;
import fa.gs.portfolio.app.base.api.ApiEndpointsExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fabio A. González Sosa
 */
@Component
public class ProductosApiEndpointsExporter implements ApiEndpointsExporter {

    @Autowired
    private AppParameters params;

    /**
     * {@inheritDoc }
     */
    @Override
    public void export() throws Throwable {
        ApiEndpointsExporter.simple("listar_producto", "http", "127.0.0.1", params.getAppPort(), "/v1/productos");
        ApiEndpointsExporter.simple("registrar_producto", "http", "127.0.0.1", params.getAppPort(), "/v1/productos");
        ApiEndpointsExporter.simple("modificar_producto", "http", "127.0.0.1", params.getAppPort(), "/v1/productos");
        ApiEndpointsExporter.simple("eliminar_producto", "http", "127.0.0.1", params.getAppPort(), "/v1/productos");
    }

}
