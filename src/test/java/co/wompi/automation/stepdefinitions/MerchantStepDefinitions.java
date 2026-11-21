package co.wompi.automation.stepdefinitions;

import co.wompi.automation.abilities.UseMerchantCredentials;
import co.wompi.automation.config.WompiConfig;
import co.wompi.automation.questions.TheResponse;
import co.wompi.automation.tasks.CreateNequiTransaction;
import io.cucumber.java.es.Dado;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class MerchantStepDefinitions {

    private static final String MERCHANT = "Comercio";

    @Dado("que el comercio tiene sus llaves del ambiente sandbox")
    public void theMerchantHasItsSandboxKeys() {
        theActorCalled(MERCHANT)
                .whoCan(CallAnApi.at(WompiConfig.baseUrl()))
                .whoCan(UseMerchantCredentials.fromEnvironment());
    }

    @Dado("que el comercio creó una transacción de {long} pesos al número Nequi {string}")
    public void theMerchantCreatedATransaction(long amountInPesos, String phoneNumber) {
        theActorInTheSpotlight().attemptsTo(CreateNequiTransaction.withValidData(amountInPesos, phoneNumber));
        // Precondición: si la creación falla, el escenario se detiene aquí con un mensaje claro
        theActorInTheSpotlight().should(seeThat("la transacción de la precondición fue creada",
                TheResponse.statusCode(), equalTo(201)));
    }
}
