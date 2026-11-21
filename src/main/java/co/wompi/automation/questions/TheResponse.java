package co.wompi.automation.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public final class TheResponse {

    private TheResponse() {
    }

    public static Question<Integer> statusCode() {
        return Question.about("el código de estado HTTP de la respuesta")
                .answeredBy(actor -> SerenityRest.lastResponse().statusCode());
    }
}
