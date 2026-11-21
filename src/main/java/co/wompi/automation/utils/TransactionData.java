package co.wompi.automation.utils;

import java.util.UUID;

/**
 * Datos de prueba comunes para las transacciones.
 */
public final class TransactionData {

    public static final String CURRENCY_COP = "COP";
    public static final String CUSTOMER_EMAIL = "qa.automation@example.com";

    private TransactionData() {
    }

    /**
     * Wompi exige una referencia única por comercio: se genera una por transacción
     * para que cada escenario sea independiente y repetible.
     */
    public static String uniqueReference() {
        return "QA-NEQUI-" + UUID.randomUUID();
    }

    /**
     * Los escenarios hablan en pesos (lenguaje de negocio); el API recibe centavos.
     */
    public static long pesosToCents(long pesos) {
        return Math.multiplyExact(pesos, 100L);
    }
}
