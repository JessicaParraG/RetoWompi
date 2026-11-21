package co.wompi.automation.stepdefinitions;

import co.wompi.automation.questions.TheTransaction;
import co.wompi.automation.tasks.WaitForTheFinalStatus;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class TransactionResultStepDefinitions {

    @Cuando("la transacción finaliza su procesamiento")
    public void theTransactionFinishesProcessing() {
        theActorInTheSpotlight().attemptsTo(WaitForTheFinalStatus.ofTheTransaction());
    }

    @Entonces("la transacción queda en estado {string}")
    public void theTransactionEndsWithStatus(String expectedStatus) {
        theActorInTheSpotlight().should(seeThat(TheTransaction.status(), equalTo(expectedStatus)));
    }

    @Entonces("la transacción queda en estado {string} con el mensaje {string}")
    public void theTransactionEndsWithStatusAndMessage(String expectedStatus, String expectedMessage) {
        theActorInTheSpotlight().should(
                seeThat(TheTransaction.status(), equalTo(expectedStatus)),
                seeThat(TheTransaction.statusMessage(), equalTo(expectedMessage)));
    }

    @Y("la transacción registra su fecha de finalización")
    public void theTransactionRegistersItsFinalizationDate() {
        theActorInTheSpotlight().should(seeThat(TheTransaction.finalizedAt(), not(blankOrNullString())));
    }
}
