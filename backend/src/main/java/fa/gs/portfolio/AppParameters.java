/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * Encapsula los parametros externos de la aplicacion.
 *
 * @author Fabio A. González Sosa
 */
@Component
public class AppParameters {

    @Autowired
    private ApplicationArguments appArgs;

    private String appName;

    private String appPort;

    /**
     * Constructor.
     */
    public AppParameters() {
        this.appName = null;
        this.appPort = null;
    }

    public String getAppName() {
        if (appName == null) {
            appName = readOption("app.name", appArgs, "");
        }
        return appName;
    }

    public String getAppPort() {
        if (appPort == null) {
            appPort = readOption("server.port", appArgs, "");
        }
        return appPort;
    }

    /**
     * Lee un argumento puntual.
     *
     * @param name Nombre de argumento.
     * @param args Argumentos externos.
     * @param fallback Valor alternativo.
     * @return Valor de argumento si fue proporcionado, caso contrario valor de
     * {@code fallback}.
     */
    private String readOption(String name, ApplicationArguments args, String fallback) {
        try {
            if (args.containsOption(name)) {
                return args.getOptionValues(name).iterator().next();
            } else {
                return fallback;
            }
        } catch (Throwable thr) {
            return fallback;
        }
    }
}
