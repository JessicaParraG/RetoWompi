package co.wompi.automation.interactions;

import co.wompi.automation.models.TransactionRequest;
import co.wompi.automation.utils.JsonBody;
import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * POST /transactions autenticado con la llave Bearer indicada.
 */
public class PostTransaction implements Interaction {

    private final TransactionRequest body;
    private final String bearerKey;

    public PostTransaction(TransactionRequest body, String bearerKey) {
        this.body = body;
        this.bearerKey = bearerKey;
    }

    public static PostTransaction authorizedWith(String bearerKey, TransactionRequest body) {
        return instrumented(PostTransaction.class, body, bearerKey);
    }

    @Override
    @Step("{0} envía la transacción a Wompi")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.to("/transactions").with(request -> request
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + bearerKey)
                .body(JsonBody.of(body))));
    }
}
