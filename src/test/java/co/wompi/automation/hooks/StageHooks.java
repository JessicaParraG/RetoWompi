package co.wompi.automation.hooks;

import co.wompi.automation.config.HttpTimeouts;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class StageHooks {

    /**
     * Cada escenario arranca con un escenario (Stage) limpio: actores nuevos y sin memoria,
     * para que ningún escenario dependa de datos de otro.
     */
    @Before(order = 0)
    public void setTheStage() {
        HttpTimeouts.applyToSerenityRest();
        OnStage.setTheStage(new OnlineCast());
    }
}
