package co.wompi.automation.config;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import net.serenitybdd.rest.SerenityRest;

/**
 * Por defecto REST Assured no tiene tiempo límite: si la red falla al conectar con Wompi,
 * la ejecución puede quedarse esperando indefinidamente (ocurrió durante el desarrollo).
 * Aquí se fija un tiempo máximo para conectar y para recibir la respuesta.
 */
public final class HttpTimeouts {

    private HttpTimeouts() {
    }

    public static void applyToSerenityRest() {
        int timeoutMillis = WompiConfig.httpTimeoutSeconds() * 1000;
        SerenityRest.setDefaultConfig(RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeoutMillis)
                        .setParam("http.socket.timeout", timeoutMillis)));
    }
}
