package co.wompi.automation.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

import java.util.List;
import java.util.Map;

/**
 * Datos del error contenidos en la última respuesta (nodo "error").
 * <pre>
 * 422: {"error":{"type":"INPUT_VALIDATION_ERROR","messages":{"campo":["mensaje", ...]}}}
 * 401: {"error":{"type":"INVALID_ACCESS_TOKEN","reason":"Llave no válida"}}
 * </pre>
 */
public final class TheError {

    private TheError() {
    }

    public static Question<String> type() {
        return Question.about("el tipo de error")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getString("error.type"));
    }

    public static Question<String> reason() {
        return Question.about("el motivo del error")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getString("error.reason"));
    }

    /**
     * Mensajes de validación de un campo. Busca de forma recursiva porque Wompi anida
     * los errores de los sub-objetos (p.ej. payment_method.messages.phone_number).
     */
    public static Question<List<String>> messagesOf(String field) {
        return Question.about("los mensajes de error del campo " + field)
                .answeredBy(actor -> {
                    Map<String, Object> messages = SerenityRest.lastResponse().jsonPath().getMap("error.messages");
                    return messages == null ? List.of() : findMessages(messages, field);
                });
    }

    private static List<String> findMessages(Map<?, ?> node, String field) {
        for (Map.Entry<?, ?> entry : node.entrySet()) {
            if (field.equals(entry.getKey()) && entry.getValue() instanceof List<?> list) {
                return list.stream().map(String::valueOf).toList();
            }
            if (entry.getValue() instanceof Map<?, ?> child) {
                List<String> nested = findMessages(child, field);
                if (!nested.isEmpty()) {
                    return nested;
                }
            }
        }
        return List.of();
    }
}
