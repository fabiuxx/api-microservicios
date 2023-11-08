/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.base.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author Fabio A. González Sosa
 */
public class ApiResponses {

    public static JsonObject ok() {
        return ok(null);
    }

    public static JsonObject ok(JsonElement payload) {
        // Estado.
        JsonObject status = new JsonObject();
        status.add("success", new JsonPrimitive(true));

        // Carga.
        if (payload == null) {
            payload = JsonNull.INSTANCE;
        }

        // Dato final.
        JsonObject envelope = new JsonObject();
        envelope.add("status", status);
        envelope.add("payload", payload);
        return envelope;
    }

    public static JsonObject ko() {
        return ko(null);
    }

    public static JsonObject ko(Throwable cause) {
        return ko(cause, null);
    }

    public static JsonObject ko(Throwable cause, JsonElement payload) {
        // Estado.
        JsonObject status = new JsonObject();
        status.add("success", new JsonPrimitive(false));
        status.add("cause", cause != null ? new JsonPrimitive(cause.getLocalizedMessage()) : JsonNull.INSTANCE);

        // Carga.
        if (payload == null) {
            payload = JsonNull.INSTANCE;
        }

        // Dato final.
        JsonObject envelope = new JsonObject();
        envelope.add("status", status);
        envelope.add("payload", payload);
        return envelope;
    }

}
