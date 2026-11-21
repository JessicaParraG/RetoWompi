package co.wompi.automation.abilities;

import co.wompi.automation.config.WompiConfig;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;

/**
 * Habilidad del actor (comercio) de usar sus llaves de Wompi.
 * Separa "qué credenciales tiene el comercio" de "cómo se comunica por HTTP" (CallAnApi).
 */
public class UseMerchantCredentials implements Ability {

    private final String publicKey;
    private final String privateKey;
    private final String integrityKey;

    private UseMerchantCredentials(String publicKey, String privateKey, String integrityKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.integrityKey = integrityKey;
    }

    public static UseMerchantCredentials fromEnvironment() {
        return new UseMerchantCredentials(
                WompiConfig.publicKey(),
                WompiConfig.privateKey(),
                WompiConfig.integrityKey());
    }

    public static UseMerchantCredentials as(Actor actor) {
        return actor.abilityTo(UseMerchantCredentials.class);
    }

    public String publicKey() {
        return publicKey;
    }

    public String privateKey() {
        return privateKey;
    }

    public String integrityKey() {
        return integrityKey;
    }

    @Override
    public String toString() {
        // Nunca exponer las llaves en logs ni en el reporte de Serenity
        return "usar las llaves del comercio en Wompi";
    }
}
