package fa.gs.portfolio;

import fa.gs.portfolio.app.base.DatabaseInitializer;
import fa.gs.portfolio.app.productos.ProductosDatabaseInitializer;
import fa.gs.portfolio.app.stock.StockDatabaseInitializer;
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
        // App: productos.
        if (Objects.equals(appParameters.getAppName(), "productos")) {
            try {
                DatabaseInitializer initializer = beansFactory.getBean(ProductosDatabaseInitializer.class);
                initializer.init();
                log.info("inicializacion de base de datos para servicio: productos [OK]");
            } catch (Throwable thr) {
                log.error(thr, "Ocurrio un error inicializando servicio de base de datos para servicio: productos.");
                throw new Exception("error", thr);
            }
        }

        // App: stock.
        if (Objects.equals(appParameters.getAppName(), "stock")) {
            try {
                DatabaseInitializer initializer = beansFactory.getBean(StockDatabaseInitializer.class);
                initializer.init();
                log.info("inicializacion de base de datos para servicio: stock [OK]");
            } catch (Throwable thr) {
                log.error(thr, "Ocurrio un error inicializando servicio de base de datos para servicio: stock.");
                throw new Exception("error", thr);
            }
        }
    }

}
