/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Fabio A. González Sosa
 */
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AppPersistence {

    @Autowired
    private AppParameters appParameters;

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Bean(destroyMethod = "close")
    @Scope(scopeName = "singleton")
    public DataSource datasource() throws IOException {
        // Inicializar driver.
        org.h2.Driver.load();

        // Path para archivo de base de datos.
        Path dbPath = Paths.get(".", "db", appParameters.getAppName(), "data");

        // Crear directorios necesarios.
        Path parent = dbPath.getParent();
        if (parent != null) {
            parent.toFile().mkdirs();
        }

        // Inicializar HikariCP.
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(org.h2.Driver.class.getCanonicalName());
        config.setJdbcUrl(String.format("jdbc:h2:file:%s;DATABASE_TO_UPPER=FALSE;IGNORECASE=FALSE;FILE_LOCK=SOCKET;USER=%s;PASSWORD=%s", dbPath.toFile().getAbsolutePath(), "admin", "admin"));
        config.setUsername("admin");
        config.setPassword("admin");
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager() throws IOException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

    @Bean
    public EntityManager entityManager() throws IOException {
        LocalContainerEntityManagerFactoryBean factory = entityManagerFactoryBean();
        EntityManagerFactory emFactory = factory.getObject();
        if (emFactory != null) {
            return emFactory.createEntityManager();
        }
        return null;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() throws IOException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
        entityManagerFactoryBean.setDataSource(datasource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        return entityManagerFactoryBean;
    }

    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        return vendorAdapter;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, org.hibernate.dialect.H2Dialect.class.getCanonicalName());
        properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, "3");
        properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, "50");
        properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, "10");
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, false);
        return properties;
    }

}
