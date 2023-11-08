/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.base.api;

import com.google.gson.JsonObject;
import fa.gs.portfolio.AppLogger;
import java.io.IOException;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Fabio A. González Sosa
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final AppLogger log = AppLogger.get(GlobalExceptionHandler.class);

    @ExceptionHandler({
        IllegalArgumentException.class,
        NullPointerException.class,
        IOException.class,
        SQLException.class
    })
    public ResponseEntity<JsonObject> handle(Exception ex, WebRequest request) {
        log.warning(ex, "Ocurrio un error procesando peticion.");
        JsonObject json = ApiResponses.ko(ex);
        return ResponseEntity.status(400).body(json);
    }

}
