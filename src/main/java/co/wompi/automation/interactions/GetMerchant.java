package co.wompi.automation.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * GET /merchants/{llave_publica}: información pública del comercio, incluidos los acceptance tokens.
 */
public class GetMerchant implements Interaction {

    private final String publicKey;

    public GetMerchant(String publicKey) {
        this.publicKey = publicKey;
    }

    public static GetMerchant withPublicKey(String publicKey) {
        return instrumented(GetMerchant.class, publicKey);
    }

    @Override
    @Step("{0} consulta la información del comercio en Wompi")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource("/merchants/{publicKey}")
                .with(request -> request.pathParam("publicKey", publicKey)));
    }
}
