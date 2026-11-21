package co.wompi.automation.tasks;

import co.wompi.automation.config.WompiConfig;
import co.wompi.automation.interactions.GetTransaction;
import co.wompi.automation.models.TransactionStatus;
import co.wompi.automation.utils.SessionKeys;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static org.awaitility.Awaitility.await;

/**
 * Nequi resuelve la transacción de forma asíncrona: se crea en PENDING y luego cambia
 * a su estado final. Esta tarea consulta GET /transactions/{id} hasta que el estado
 * sea final o se agote WOMPI_POLLING_TIMEOUT_SECONDS. Al terminar, la última respuesta
 * (con el estado final) queda disponible para las Questions.
 */
public class WaitForTheFinalStatus implements Task {

    public static WaitForTheFinalStatus ofTheTransaction() {
        return instrumented(WaitForTheFinalStatus.class);
    }

    @Override
    @Step("{0} espera a que la transacción finalice su procesamiento")
    public <T extends Actor> void performAs(T actor) {
        String transactionId = actor.recall(SessionKeys.TRANSACTION_ID);
        if (transactionId == null) {
            throw new IllegalStateException("No hay una transacción creada previamente para esperar su estado final");
        }

        try {
            await("estado final de la transacción " + transactionId)
                    .atMost(Duration.ofSeconds(WompiConfig.pollingTimeoutSeconds()))
                    .pollDelay(Duration.ZERO)
                    .pollInterval(Duration.ofMillis(WompiConfig.pollingIntervalMillis()))
                    // Serenity guarda la última respuesta por hilo: se consulta en el mismo hilo del escenario
                    .pollInSameThread()
                    .until(() -> {
                        actor.attemptsTo(GetTransaction.withId(transactionId));
                        return currentStatus().isFinal();
                    });
        } catch (ConditionTimeoutException e) {
            throw new AssertionError("La transacción " + transactionId + " siguió en PENDING después de "
                    + WompiConfig.pollingTimeoutSeconds() + " segundos", e);
        }
    }

    private static TransactionStatus currentStatus() {
        String status = SerenityRest.lastResponse().jsonPath().getString("data.status");
        return status == null ? TransactionStatus.PENDING : TransactionStatus.valueOf(status);
    }
}
