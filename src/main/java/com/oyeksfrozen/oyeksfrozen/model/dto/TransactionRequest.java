package com.oyeksfrozen.oyeksfrozen.model.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String email;
    private int amount;

}
