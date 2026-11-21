package co.wompi.automation.models;

/**
 * Estados de una transacción en Wompi.
 */
public enum TransactionStatus {
    PENDING,
    APPROVED,
    DECLINED,
    VOIDED,
    ERROR;

    public boolean isFinal() {
        return this != PENDING;
    }
}
