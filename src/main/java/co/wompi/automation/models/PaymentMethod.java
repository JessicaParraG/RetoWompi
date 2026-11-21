package co.wompi.automation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethod {

    public static final String NEQUI = "NEQUI";

    @JsonProperty("type")
    private final String type;

    @JsonProperty("phone_number")
    private final String phoneNumber;

    public static PaymentMethod nequi(String phoneNumber) {
        return PaymentMethod.builder().type(NEQUI).phoneNumber(phoneNumber).build();
    }
}
