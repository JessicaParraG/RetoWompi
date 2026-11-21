package co.wompi.automation.tasks;

import co.wompi.automation.abilities.UseMerchantCredentials;
import co.wompi.automation.interactions.PostTransaction;
import co.wompi.automation.models.AcceptanceTokens;
import co.wompi.automation.models.PaymentMethod;
import co.wompi.automation.models.TransactionRequest;
import co.wompi.automation.utils.IntegritySignature;
import co.wompi.automation.utils.SessionKeys;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static co.wompi.automation.utils.TransactionData.CURRENCY_COP;
import static co.wompi.automation.utils.TransactionData.CUSTOMER_EMAIL;
import static co.wompi.automation.utils.TransactionData.pesosToCents;
import static co.wompi.automation.utils.TransactionData.uniqueReference;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Crea una transacción Nequi siguiendo el flujo que exige Wompi:
 * <ol>
 *   <li>Obtener acceptance tokens nuevos (son de un solo uso)</li>
 *   <li>Generar una referencia única</li>
 *   <li>Firmar con SHA-256 usando la llave de integridad</li>
 *   <li>Enviar POST /transactions con la llave pública</li>
 * </ol>
 * Cada variante altera un único aspecto del flujo para los escenarios negativos.
 */
public class CreateNequiTransaction implements Task {

    private static final String WRONG_INTEGRITY_KEY = "stagtest_integrity_llaveQueNoEsDelComercio";

    public enum Variant {
        VALID("con datos válidos"),
        WITHOUT_ACCEPTANCE_TOKEN("sin acceptance_token"),
        INVALID_SIGNATURE("con una firma de integridad inválida");

        private final String description;

        Variant(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private final Long amountInPesos;
    private final String phoneNumber;
    private final Variant variant;

    public CreateNequiTransaction(Long amountInPesos, String phoneNumber, Variant variant) {
        this.amountInPesos = amountInPesos;
        this.phoneNumber = phoneNumber;
        this.variant = variant;
    }

    public static CreateNequiTransaction withValidData(long amountInPesos, String phoneNumber) {
        return instrumented(CreateNequiTransaction.class, amountInPesos, phoneNumber, Variant.VALID);
    }

    public static CreateNequiTransaction withoutAcceptanceToken(long amountInPesos, String phoneNumber) {
        return instrumented(CreateNequiTransaction.class, amountInPesos, phoneNumber, Variant.WITHOUT_ACCEPTANCE_TOKEN);
    }

    public static CreateNequiTransaction withInvalidSignature(long amountInPesos, String phoneNumber) {
        return instrumented(CreateNequiTransaction.class, amountInPesos, phoneNumber, Variant.INVALID_SIGNATURE);
    }

    @Override
    @Step("{0} crea una transacción Nequi de #amountInPesos pesos al número #phoneNumber #variant")
    public <T extends Actor> void performAs(T actor) {
        UseMerchantCredentials credentials = UseMerchantCredentials.as(actor);
        long amountInCents = pesosToCents(amountInPesos);
        String reference = uniqueReference();

        TransactionRequest.TransactionRequestBuilder request = TransactionRequest.builder()
                .amountInCents(amountInCents)
                .currency(CURRENCY_COP)
                .customerEmail(CUSTOMER_EMAIL)
                .reference(reference)
                .paymentMethod(PaymentMethod.nequi(phoneNumber));

        if (variant != Variant.WITHOUT_ACCEPTANCE_TOKEN) {
            actor.attemptsTo(ObtainAcceptanceTokens.ofTheMerchant());
            AcceptanceTokens tokens = actor.recall(SessionKeys.ACCEPTANCE_TOKENS);
            request.acceptanceToken(tokens.acceptanceToken())
                    .acceptPersonalAuth(tokens.personalDataAuthToken());
        }

        // La firma inválida simula a un tercero que no conoce la llave de integridad del comercio
        String integrityKey = variant == Variant.INVALID_SIGNATURE ? WRONG_INTEGRITY_KEY : credentials.integrityKey();
        request.signature(IntegritySignature.of(reference, amountInCents, CURRENCY_COP, integrityKey));

        TransactionRequest body = request.build();
        actor.remember(SessionKeys.TRANSACTION_REQUEST, body);
        actor.attemptsTo(PostTransaction.authorizedWith(credentials.publicKey(), body));

        if (SerenityRest.lastResponse().statusCode() == 201) {
            actor.remember(SessionKeys.TRANSACTION_ID, SerenityRest.lastResponse().jsonPath().getString("data.id"));
        }
    }
}
