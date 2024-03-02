package com.oyeksfrozen.oyeksfrozen.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaystackTransactionData {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("status")
    private String status;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("message")
    private String message;

    @JsonProperty("gateway_response")
    private String gatewayResponse;

    @JsonProperty("paid_at")
    private String paidAt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("metadata")
    private String metadata;

    @JsonProperty("fees")
    private int fees;

}
