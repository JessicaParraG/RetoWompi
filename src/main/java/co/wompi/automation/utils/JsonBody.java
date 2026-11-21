package co.wompi.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serializa los modelos con Jackson de forma explícita.
 * Gson y Jackson están ambos en el classpath (dependencias transitivas de Serenity);
 * serializar aquí evita depender de cuál elija REST Assured y garantiza que se
 * respeten las anotaciones @JsonProperty / @JsonInclude.
 */
public final class JsonBody {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonBody() {
    }

    public static String of(Object body) {
        try {
            return MAPPER.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No fue posible serializar el body a JSON", e);
        }
    }
}
