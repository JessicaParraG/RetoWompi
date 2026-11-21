package co.wompi.automation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Cuerpo del request de POST /transactions.
 * Los campos nulos no se serializan, lo que permite construir requests incompletos
 * para los escenarios negativos (p.ej. sin acceptance_token).
 */
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {

    @JsonProperty("acceptance_token")
    private final String acceptanceToken;

    @JsonProperty("accept_personal_auth")
    private final String acceptPersonalAuth;

    @JsonProperty("amount_in_cents")
    private final Long amountInCents;

    @JsonProperty("currency")
    private final String currency;

    @JsonProperty("signature")
    private final String signature;

    @JsonProperty("customer_email")
    private final String customerEmail;

    @JsonProperty("reference")
    private final String reference;

    @JsonProperty("payment_method")
    private final PaymentMethod paymentMethod;
}
