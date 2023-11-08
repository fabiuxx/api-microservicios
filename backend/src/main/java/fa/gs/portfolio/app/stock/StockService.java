/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock;

import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.stock.entities.Stock;
import java.io.IOException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fabio A. González Sosa
 */
@Service
public class StockService {

    private final static AppLogger log = AppLogger.get(StockService.class);

    @Autowired
    private EntityManager em;

    @Transactional(transactionManager = "jpaTransactionManager")
    public Integer registrar(Integer idProducto, Integer precio, Integer stock) throws IOException {
        try {
            Stock model = new Stock();
            model.setIdProducto(idProducto);
            model.setPrecio(precio);
            model.setStock(stock);
            em.joinTransaction();
            em.persist(model);
            em.flush();
            return model.getId();
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error registrando stock de producto.", thr);
        }
    }

}
