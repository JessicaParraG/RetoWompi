package co.wompi.automation.tasks;

import co.wompi.automation.interactions.GetTransaction;
import co.wompi.automation.interactions.GetTransactionsByReference;
import co.wompi.automation.models.TransactionRequest;
import co.wompi.automation.utils.SessionKeys;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

/**
 * Consultas sobre la transacción que el actor creó previamente en el escenario.
 */
public final class ConsultTransaction {

    private ConsultTransaction() {
    }

    public static Performable byItsId() {
        return Task.where("{0} consulta la transacción creada por su ID",
                (Actor actor) -> actor.attemptsTo(
                        GetTransaction.withId(actor.recall(SessionKeys.TRANSACTION_ID))));
    }

    public static Performable byItsReferenceUsingTheKey(String privateKey) {
        return Task.where("{0} consulta las transacciones con la referencia de la transacción creada",
                (Actor actor) -> {
                    TransactionRequest request = actor.recall(SessionKeys.TRANSACTION_REQUEST);
                    actor.attemptsTo(GetTransactionsByReference.authorizedWith(privateKey, request.getReference()));
                });
    }
}
