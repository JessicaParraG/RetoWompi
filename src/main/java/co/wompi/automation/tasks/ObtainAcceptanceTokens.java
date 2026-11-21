package co.wompi.automation.tasks;

import co.wompi.automation.abilities.UseMerchantCredentials;
import co.wompi.automation.interactions.GetMerchant;
import co.wompi.automation.models.AcceptanceTokens;
import co.wompi.automation.utils.SessionKeys;
import io.restassured.path.json.JsonPath;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Obtiene un par NUEVO de acceptance tokens. Debe ejecutarse antes de cada transacción
 * porque Wompi rechaza un token que ya fue usado ("El token de aceptación ya fue usado").
 */
public class ObtainAcceptanceTokens implements Task {

    public static ObtainAcceptanceTokens ofTheMerchant() {
        return instrumented(ObtainAcceptanceTokens.class);
    }

    @Override
    @Step("{0} obtiene los tokens de aceptación de términos y datos personales")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(GetMerchant.withPublicKey(UseMerchantCredentials.as(actor).publicKey()));

        int statusCode = SerenityRest.lastResponse().statusCode();
        if (statusCode != 200) {
            throw new IllegalStateException(
                    "No fue posible obtener los acceptance tokens del comercio. HTTP " + statusCode
                            + ": " + SerenityRest.lastResponse().asString());
        }

        JsonPath json = SerenityRest.lastResponse().jsonPath();
        actor.remember(SessionKeys.ACCEPTANCE_TOKENS, new AcceptanceTokens(
                json.getString("data.presigned_acceptance.acceptance_token"),
                json.getString("data.presigned_personal_data_auth.acceptance_token")));
    }
}
