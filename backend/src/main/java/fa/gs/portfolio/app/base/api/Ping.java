/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.base.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador rest.
 *
 * @author Fabio A. González Sosa
 */
@RestController
public class Ping {

    /**
     * Permite conocer si el servicio esta activo.
     *
     * @return Siempre retorna {@code pong}.
     */
    @GetMapping(path = "/v1/ping")
    public String ping() {
        return "pong";
    }

}
