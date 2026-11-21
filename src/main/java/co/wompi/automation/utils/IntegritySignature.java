package co.wompi.automation.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Firma de integridad exigida por Wompi al crear una transacción:
 * SHA256("&lt;referencia&gt;&lt;monto_en_centavos&gt;&lt;moneda&gt;&lt;llave_de_integridad&gt;") en hexadecimal.
 */
public final class IntegritySignature {

    private IntegritySignature() {
    }

    public static String of(String reference, long amountInCents, String currency, String integrityKey) {
        String chain = reference + amountInCents + currency + integrityKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(chain.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible en la JVM", e);
        }
    }
}
