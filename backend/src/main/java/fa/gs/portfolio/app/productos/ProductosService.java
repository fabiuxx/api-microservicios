/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.productos;

import fa.gs.portfolio.AppLogger;
import fa.gs.portfolio.app.productos.entities.Producto;
import java.io.IOException;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fabio A. González Sosa
 */
@Component
public class ProductosService {

    private final static AppLogger log = AppLogger.get(ProductosService.class);

    @Autowired
    private EntityManager em;

    public Collection<Producto> listar() throws IOException {
        try {
            Query query = em.createQuery("select p from Producto p", Producto.class);
            return query.getResultList();
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error listando productos.", thr);
        }
    }

    @Transactional(transactionManager = "jpaTransactionManager")
    public Integer registrar(String nombre, String descripcion, String categoria) throws IOException {
        try {
            Producto model = new Producto();
            model.setNombre(nombre);
            model.setDescripcion(descripcion);
            model.setCategoria(categoria);
            em.joinTransaction();
            em.persist(model);
            em.flush();
            return model.getId();
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error registrando producto.", thr);
        }
    }

    @Transactional(transactionManager = "jpaTransactionManager")
    public Integer modificar(Integer id, String nombre, String descripcion, String categoria) throws IOException {
        try {
            Producto model = em.find(Producto.class, id);
            if (model == null) {
                throw new IllegalArgumentException("No existe el producto indicado.");
            }

            model.setNombre(nombre);
            model.setDescripcion(descripcion);
            model.setCategoria(categoria);
            em.joinTransaction();
            em.persist(model);
            em.flush();
            return model.getId();
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error modificando producto.", thr);
        }
    }

    @Transactional(transactionManager = "jpaTransactionManager")
    public void eliminar(Integer id) throws IOException {
        try {
            Producto model = em.find(Producto.class, id);
            if (model != null) {
                em.joinTransaction();
                em.remove(model);
                em.flush();
            }
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error eliminando producto.", thr);
        }
    }

}
