/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.base;

/**
 * Interface que abstrae un servicio de base de datos.
 *
 * @author Fabio A. González Sosa
 */
public abstract class DatabaseService {

    /**
     * Punto para inicializacion de servicio.
     *
     * @throws Throwable Si ocurre algun error.
     */
    abstract public void init() throws Throwable;

}
