package co.wompi.automation.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * GET /transactions?reference={referencia}: endpoint privado, exige la llave privada del comercio.
 */
public class GetTransactionsByReference implements Interaction {

    private final String reference;
    private final String bearerKey;

    public GetTransactionsByReference(String reference, String bearerKey) {
        this.reference = reference;
        this.bearerKey = bearerKey;
    }

    public static GetTransactionsByReference authorizedWith(String bearerKey, String reference) {
        return instrumented(GetTransactionsByReference.class, reference, bearerKey);
    }

    @Override
    @Step("{0} consulta las transacciones con la referencia #reference")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource("/transactions").with(request -> request
                .queryParam("reference", reference)
                .header("Authorization", "Bearer " + bearerKey)));
    }
}
