package co.wompi.automation.tasks;

import co.wompi.automation.abilities.UseMerchantCredentials;
import co.wompi.automation.interactions.PostTransaction;
import co.wompi.automation.models.AcceptanceTokens;
import co.wompi.automation.models.TransactionRequest;
import co.wompi.automation.utils.SessionKeys;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Intenta crear una transacción enviando únicamente un acceptance_token válido,
 * omitiendo monto, moneda, referencia, firma y método de pago.
 */
public class CreateTransactionWithOnlyAcceptanceToken implements Task {

    public static CreateTransactionWithOnlyAcceptanceToken now() {
        return instrumented(CreateTransactionWithOnlyAcceptanceToken.class);
    }

    @Override
    @Step("{0} intenta crear una transacción enviando únicamente el acceptance_token")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(ObtainAcceptanceTokens.ofTheMerchant());
        AcceptanceTokens tokens = actor.recall(SessionKeys.ACCEPTANCE_TOKENS);

        TransactionRequest body = TransactionRequest.builder()
                .acceptanceToken(tokens.acceptanceToken())
                .build();

        actor.remember(SessionKeys.TRANSACTION_REQUEST, body);
        actor.attemptsTo(PostTransaction.authorizedWith(UseMerchantCredentials.as(actor).publicKey(), body));
    }
}
