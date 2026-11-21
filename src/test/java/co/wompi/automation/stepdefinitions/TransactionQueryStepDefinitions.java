package co.wompi.automation.stepdefinitions;

import co.wompi.automation.questions.TheError;
import co.wompi.automation.questions.TheResponse;
import co.wompi.automation.questions.TheTransaction;
import co.wompi.automation.tasks.ConsultTransaction;
import co.wompi.automation.utils.SessionKeys;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class TransactionQueryStepDefinitions {

    private static final String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";

    @Cuando("el comercio consulta la transacción por su ID")
    public void consultsTheTransactionById() {
        theActorInTheSpotlight().attemptsTo(ConsultTransaction.byItsId());
    }

    @Cuando("el comercio consulta las transacciones con esa referencia usando la llave privada {string}")
    public void consultsTheTransactionsByReferenceUsingThePrivateKey(String privateKey) {
        theActorInTheSpotlight().attemptsTo(ConsultTransaction.byItsReferenceUsingTheKey(privateKey));
    }

    @Entonces("la consulta retorna la transacción solicitada")
    public void theQueryReturnsTheRequestedTransaction() {
        String expectedId = theActorInTheSpotlight().recall(SessionKeys.TRANSACTION_ID);
        theActorInTheSpotlight().should(
                seeThat(TheResponse.statusCode(), equalTo(200)),
                seeThat(TheTransaction.id(), equalTo(expectedId)));
    }

    @Entonces("la solicitud es rechazada por autenticación con el motivo {string}")
    public void theRequestIsRejectedForAuthentication(String expectedReason) {
        theActorInTheSpotlight().should(
                seeThat(TheResponse.statusCode(), equalTo(401)),
                seeThat(TheError.type(), equalTo(INVALID_ACCESS_TOKEN)),
                seeThat(TheError.reason(), equalTo(expectedReason)));
    }
}
