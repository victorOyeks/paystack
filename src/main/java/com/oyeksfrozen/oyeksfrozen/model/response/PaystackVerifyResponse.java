package com.oyeksfrozen.oyeksfrozen.model.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oyeksfrozen.oyeksfrozen.model.dto.PaystackTransactionData;
import lombok.Data;

@Data
public class PaystackVerifyResponse {
    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private PaystackTransactionData data;

}
