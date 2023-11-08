/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.base;

import fa.gs.portfolio.AppLogger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Facade para operaciones generales con base de datos.
 *
 * @author Fabio A. González Sosa
 */
@Component
public class DatabaseFacade {

    private final static AppLogger log = AppLogger.get(DatabaseFacade.class);

    @Autowired
    private EntityManager em;

    /**
     * Ejecuta una sentencia de tipo SELECT.
     *
     * @param query Setencia a ejecutar.
     * @return Registros retornados, si hubiere.
     * @throws SQLException Si no es posible realizar la operacion.
     */
    public List<Map<String, Object>> executeSelect(String query) throws SQLException {
        org.hibernate.query.Query hquery = em.createNativeQuery(query).unwrap(org.hibernate.query.Query.class);
        hquery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        return hquery.getResultList();
    }

    /**
     * Ejecuta una sentencia de modificacion, ej.: INSERT/UPDATE/DELETE.
     *
     * @param query Sentencia a ejecutar.
     * @return Cantidad de registros modificados, si hubiere.
     * @throws SQLException Si no es posible realizar la operacion.
     */
    public int executeUpdate(String query) throws SQLException {
        em.joinTransaction();
        org.hibernate.query.Query hquery = em.createNativeQuery(query).unwrap(org.hibernate.query.Query.class);
        return hquery.executeUpdate();
    }

    /**
     * Verifica si un esquema existe en la base de datos.
     *
     * @param name Nombre de esquema a verificar.
     * @return {@code true} si el esquema existe, caso contrrio {@code false}.
     * @throws IOException Si no es posible realizar la operacion.
     */
    public boolean schemaExists(String name) throws IOException {
        try {
            String query = String.format("SELECT EXISTS(SELECT 1 FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '%s') AS \"E\"", name);
            List<Map<String, Object>> rows = executeSelect(query);

            // Control.
            if (rows.isEmpty()) {
                return false;
            }

            // Control.
            Map<String, Object> row = rows.get(0);
            if (!row.containsKey("E")) {
                return false;
            }

            // Ok.
            return Boolean.parseBoolean(row.get("E").toString());
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error verificando esquema.", thr);
        }
    }

    /**
     * Crea un esquema de base de datos.
     *
     * @param name Nombre de esquema.
     * @throws IOException Si no es posible realizar la operacion.
     */
    public void createSchema(String name) throws IOException {
        try {
            String query = String.format("CREATE SCHEMA \"%s\"", name);
            executeUpdate(query);
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error creando esquema.", thr);
        }
    }

    /**
     * Verifica si una tabla existe en la base de datos.
     *
     * @param schema Nombre de esquema.
     * @param name Nombre de tabla.
     * @return {@code true} si la tabla existe, caso contrario {@code false}.
     * @throws IOException Si no es posible realizar la operacion.
     */
    public boolean tableExists(String schema, String name) throws IOException {
        try {
            String query = String.format("SELECT EXISTS(SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s') AS \"E\"", schema, name);
            List<Map<String, Object>> rows = executeSelect(query);

            // Control.
            if (rows.isEmpty()) {
                return false;
            }

            // Control.
            Map<String, Object> row = rows.get(0);
            if (!row.containsKey("E")) {
                return false;
            }

            // Ok.
            return Boolean.parseBoolean(row.get("E").toString());
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error verificando tabla.", thr);
        }
    }

    /**
     * Permite crear una nueva tabla en base de datos.
     *
     * @param schema Nombre de esquema.
     * @param name Nombre de tabla.
     * @param columnDefs Definiciones de columnas.
     * @throws IOException Si no es posible realizar la operacio.
     */
    public void createTable(String schema, String name, String[] columnDefs) throws IOException {
        try {
            // Generar query DDL para creacion de tabla, lo mas simple posible.
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE \"").append(schema).append("\".\"").append(name).append("\" (");
            if (columnDefs != null) {
                for (int i = 0; i < columnDefs.length; i++) {
                    builder.append(columnDefs[i]);
                    if (i != columnDefs.length - 1) {
                        builder.append(", ");
                    }
                }
            }
            builder.append(")");

            // Crear tabla.
            String query = builder.toString();
            executeUpdate(query);
        } catch (Throwable thr) {
            throw new IOException("Ocurrio un error creando tabla.", thr);
        }
    }

}
