package co.wompi.automation.stepdefinitions;

import co.wompi.automation.models.TransactionRequest;
import co.wompi.automation.questions.TheError;
import co.wompi.automation.questions.TheResponse;
import co.wompi.automation.questions.TheTransaction;
import co.wompi.automation.tasks.CreateNequiTransaction;
import co.wompi.automation.tasks.CreateTransactionWithOnlyAcceptanceToken;
import co.wompi.automation.utils.SessionKeys;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class TransactionCreationStepDefinitions {

    private static final String INPUT_VALIDATION_ERROR = "INPUT_VALIDATION_ERROR";
    private static final String NOT_PRESENT = "No está presente";

    @Cuando("el comercio crea una transacción de {long} pesos al número Nequi {string}")
    public void createsATransaction(long amountInPesos, String phoneNumber) {
        theActorInTheSpotlight().attemptsTo(CreateNequiTransaction.withValidData(amountInPesos, phoneNumber));
    }

    @Cuando("el comercio intenta crear una transacción de {long} pesos al número Nequi {string} sin acceptance_token")
    public void triesToCreateATransactionWithoutAcceptanceToken(long amountInPesos, String phoneNumber) {
        theActorInTheSpotlight().attemptsTo(CreateNequiTransaction.withoutAcceptanceToken(amountInPesos, phoneNumber));
    }

    @Cuando("el comercio intenta crear una transacción enviando únicamente el acceptance_token")
    public void triesToCreateATransactionWithOnlyTheAcceptanceToken() {
        theActorInTheSpotlight().attemptsTo(CreateTransactionWithOnlyAcceptanceToken.now());
    }

    @Cuando("el comercio intenta crear una transacción de {long} pesos al número Nequi {string} con una firma inválida")
    public void triesToCreateATransactionWithAnInvalidSignature(long amountInPesos, String phoneNumber) {
        theActorInTheSpotlight().attemptsTo(CreateNequiTransaction.withInvalidSignature(amountInPesos, phoneNumber));
    }

    @Entonces("la transacción se crea en estado {string}")
    public void theTransactionIsCreatedWithStatus(String expectedStatus) {
        theActorInTheSpotlight().should(
                seeThat(TheResponse.statusCode(), equalTo(201)),
                seeThat(TheTransaction.status(), equalTo(expectedStatus)));
    }

    @Y("Wompi asigna un identificador a la transacción")
    public void wompiAssignsAnIdentifier() {
        theActorInTheSpotlight().should(seeThat(TheTransaction.id(), not(blankOrNullString())));
    }

    @Y("la transacción registra el método de pago {string} con el número {string}")
    public void theTransactionRegistersThePaymentMethod(String expectedType, String expectedPhoneNumber) {
        theActorInTheSpotlight().should(
                seeThat(TheTransaction.paymentMethodType(), equalTo(expectedType)),
                seeThat(TheTransaction.phoneNumber(), equalTo(expectedPhoneNumber)));
    }

    @Y("la transacción conserva el monto, la moneda y la referencia enviados")
    public void theTransactionKeepsTheSentData() {
        TransactionRequest sent = theActorInTheSpotlight().recall(SessionKeys.TRANSACTION_REQUEST);
        theActorInTheSpotlight().should(
                seeThat(TheTransaction.amountInCents(), equalTo(sent.getAmountInCents())),
                seeThat(TheTransaction.currency(), equalTo(sent.getCurrency())),
                seeThat(TheTransaction.reference(), equalTo(sent.getReference())));
    }

    @Entonces("la transacción es rechazada por datos inválidos")
    public void theTransactionIsRejectedForInvalidData() {
        theActorInTheSpotlight().should(
                seeThat(TheResponse.statusCode(), equalTo(422)),
                seeThat(TheError.type(), equalTo(INPUT_VALIDATION_ERROR)));
    }

    @Y("el error del campo {string} indica {string}")
    public void theFieldErrorIndicates(String field, String expectedMessage) {
        theActorInTheSpotlight().should(seeThat(TheError.messagesOf(field), hasItem(expectedMessage)));
    }

    @Y("se informan como no presentes los campos:")
    public void theFieldsAreReportedAsNotPresent(List<String> fields) {
        fields.forEach(field -> theActorInTheSpotlight().should(
                seeThat(TheError.messagesOf(field), hasItem(NOT_PRESENT))));
    }
}
