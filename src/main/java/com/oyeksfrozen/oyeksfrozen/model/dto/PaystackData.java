package com.oyeksfrozen.oyeksfrozen.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaystackData {
    @JsonProperty("authorization_url")
    private String authorizationUrl;

    @JsonProperty("access_code")
    private String accessCode;

    @JsonProperty("reference")
    private String reference;
}
