package co.wompi.automation.utils;

/**
 * Llaves con las que el actor recuerda información entre pasos (actor.remember / actor.recall).
 */
public final class SessionKeys {

    public static final String ACCEPTANCE_TOKENS = "ACCEPTANCE_TOKENS";
    public static final String TRANSACTION_REQUEST = "TRANSACTION_REQUEST";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";

    private SessionKeys() {
    }
}
