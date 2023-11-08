/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.productos;

import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.base.DatabaseFacade;
import fa.gs.portfolio.app.base.DatabaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fabio A. González Sosa
 */
@Component
public class ProductosDatabaseInitializer implements DatabaseInitializer {

    private final static AppLogger log = AppLogger.get(ProductosDatabaseInitializer.class);

    @Autowired
    private DatabaseFacade db;

    @Override
    @Transactional(transactionManager = "jpaTransactionManager")
    public void init() throws Throwable {
        // Esquema principal.
        boolean schemaExists = db.schemaExists("app");
        if (!schemaExists) {
            db.createSchema("app");
        }

        // Tabla principal.
        boolean tableExists = db.tableExists("app", "producto");
        if (!tableExists) {
            db.createTable("app", "producto", new String[]{
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY",
                "nombre TEXT",
                "descripcion TEXT",
                "categoria TEXT"
            });
        }
    }

}
