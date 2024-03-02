package com.oyeksfrozen.oyeksfrozen.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oyeksfrozen.oyeksfrozen.model.dto.PaystackData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaystackResponse {
    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private PaystackData data;
}
