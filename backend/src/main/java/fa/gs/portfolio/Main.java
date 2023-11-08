package fa.gs.portfolio;

import fa.gs.portfolio.app.base.DatabaseService;
import fa.gs.portfolio.app.productos.ProductosDatabaseService;
import fa.gs.portfolio.app.stock.StockDatabaseService;
import java.util.Objects;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main implements ApplicationRunner {

    private final static AppLogger log = AppLogger.get(Main.class);

    @Autowired
    private BeanFactory beansFactory;

    @Autowired
    private AppParameters appParameters;

    /**
     * Punto de entrada.
     *
     * @param args Argumentos de linea de comandos.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Inicializar parametros generales.
        log.info("Inicializando parametros de aplicacion ...");

        // App: productos.
        if (Objects.equals(appParameters.getAppName(), "productos")) {
            try {
                DatabaseService databaseService = beansFactory.getBean(ProductosDatabaseService.class);
                databaseService.init();
                log.info("servicio de base de datos para servicio: productos [OK]");
            } catch (Throwable thr) {
                log.error(thr, "Ocurrio un error inicializando servicio de base de datos para servicio: productos.");
                throw new Exception("error", thr);
            }
        }

        // App: stock.
        if (Objects.equals(appParameters.getAppName(), "stock")) {
            try {
                DatabaseService databaseService = beansFactory.getBean(StockDatabaseService.class);
                databaseService.init();
                log.info("servicio de base de datos para servicio: stock [OK]");
            } catch (Throwable thr) {
                log.error(thr, "Ocurrio un error inicializando servicio de base de datos para servicio: stock.");
                throw new Exception("error", thr);
            }
        }
    }

}
