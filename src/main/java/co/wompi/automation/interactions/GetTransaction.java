package co.wompi.automation.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * GET /transactions/{id}: endpoint público de Wompi, no requiere llave.
 */
public class GetTransaction implements Interaction {

    private final String transactionId;

    public GetTransaction(String transactionId) {
        this.transactionId = transactionId;
    }

    public static GetTransaction withId(String transactionId) {
        return instrumented(GetTransaction.class, transactionId);
    }

    @Override
    @Step("{0} consulta la transacción #transactionId")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource("/transactions/{id}")
                .with(request -> request.pathParam("id", transactionId)));
    }
}
