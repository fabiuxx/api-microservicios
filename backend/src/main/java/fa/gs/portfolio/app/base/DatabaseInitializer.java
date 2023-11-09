/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fa.gs.portfolio.app.base;

/**
 * Permite abstraer procedimientos de inicializacion respecto a la base de datos
 * a utilizar.
 *
 * @author Fabio A. González Sosa
 */
public interface DatabaseInitializer {

    /**
     * Punto para inicializacion de servicio.
     *
     * @throws Throwable Si ocurre algun error.
     */
    void init() throws Throwable;

}
