/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock;

import fa.gs.portfolio.AppLogger;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fabio A. González Sosa
 */
@Component
public class StockDatabaseService extends fa.gs.portfolio.app.base.DatabaseService {

    private final static AppLogger log = AppLogger.get(StockDatabaseService.class);

    @Autowired
    private DataSource ds;

    @Autowired
    private EntityManager em;

    @Override
    public void init() throws Throwable {
        log.info("with ds? %s", ds != null);
        log.info("with em? %s", em != null);
    }

}
