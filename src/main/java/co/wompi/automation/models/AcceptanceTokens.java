package co.wompi.automation.models;

/**
 * Tokens prefirmados que el comercio obtiene de GET /merchants/{llave_publica}.
 * Cada token solo puede usarse en UNA transacción (hallazgo de la exploración),
 * por eso se obtienen nuevamente antes de cada creación.
 *
 * @param acceptanceToken       aceptación de los términos y condiciones (END_USER_POLICY)
 * @param personalDataAuthToken autorización de tratamiento de datos personales (PERSONAL_DATA_AUTH)
 */
public record AcceptanceTokens(String acceptanceToken, String personalDataAuthToken) {
}
