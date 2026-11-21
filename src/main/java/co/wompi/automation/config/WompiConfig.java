package co.wompi.automation.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Optional;

/**
 * Punto único de acceso a la configuración del framework.
 * <p>
 * Orden de resolución de cada variable:
 * <ol>
 *   <li>Variable de entorno del sistema operativo (útil en CI/CD)</li>
 *   <li>Archivo {@code .env} en la raíz del proyecto (desarrollo local)</li>
 *   <li>Valor por defecto (solo para variables opcionales)</li>
 * </ol>
 * Si falta una variable obligatoria se lanza una excepción con un mensaje claro
 * en lugar de fallar más adelante con un 401/422 difícil de diagnosticar.
 */
public final class WompiConfig {

    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private WompiConfig() {
    }

    public static String baseUrl() {
        return required("WOMPI_BASE_URL").replaceAll("/+$", "");
    }

    public static String publicKey() {
        return required("WOMPI_PUBLIC_KEY");
    }

    public static String privateKey() {
        return required("WOMPI_PRIVATE_KEY");
    }

    public static String integrityKey() {
        return required("WOMPI_INTEGRITY_KEY");
    }

    public static int httpTimeoutSeconds() {
        return optionalInt("WOMPI_HTTP_TIMEOUT_SECONDS", 30);
    }

    public static int pollingTimeoutSeconds() {
        return optionalInt("WOMPI_POLLING_TIMEOUT_SECONDS", 30);
    }

    public static long pollingIntervalMillis() {
        return optionalInt("WOMPI_POLLING_INTERVAL_MILLIS", 2000);
    }

    private static Optional<String> lookup(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = DOTENV.get(name);
        }
        return Optional.ofNullable(value).map(String::trim).filter(v -> !v.isEmpty());
    }

    private static String required(String name) {
        return lookup(name).orElseThrow(() -> new IllegalStateException(
                "Falta la variable de entorno obligatoria '" + name + "'. "
                        + "Defínela en el archivo .env (ver .env.example) o como variable del sistema."));
    }

    private static int optionalInt(String name, int defaultValue) {
        return lookup(name).map(value -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalStateException(
                        "La variable '" + name + "' debe ser un número entero, valor recibido: '" + value + "'", e);
            }
        }).orElse(defaultValue);
    }
}
