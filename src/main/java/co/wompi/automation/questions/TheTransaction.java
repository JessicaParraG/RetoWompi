package co.wompi.automation.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

/**
 * Datos de la transacción contenidos en la última respuesta (nodo "data").
 */
public final class TheTransaction {

    private TheTransaction() {
    }

    public static Question<String> id() {
        return field("el ID de la transacción", "data.id");
    }

    public static Question<String> status() {
        return field("el estado de la transacción", "data.status");
    }

    public static Question<String> statusMessage() {
        return field("el mensaje de estado de la transacción", "data.status_message");
    }

    public static Question<Long> amountInCents() {
        return Question.about("el monto en centavos de la transacción")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getLong("data.amount_in_cents"));
    }

    public static Question<String> currency() {
        return field("la moneda de la transacción", "data.currency");
    }

    public static Question<String> reference() {
        return field("la referencia de la transacción", "data.reference");
    }

    public static Question<String> paymentMethodType() {
        return field("el método de pago de la transacción", "data.payment_method_type");
    }

    public static Question<String> phoneNumber() {
        return field("el número Nequi de la transacción", "data.payment_method.phone_number");
    }

    public static Question<String> finalizedAt() {
        return field("la fecha de finalización de la transacción", "data.finalized_at");
    }

    private static Question<String> field(String description, String jsonPath) {
        return Question.about(description)
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getString(jsonPath));
    }
}
