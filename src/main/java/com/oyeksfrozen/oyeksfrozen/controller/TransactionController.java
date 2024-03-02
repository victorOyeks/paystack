package com.oyeksfrozen.oyeksfrozen.controller;

import com.oyeksfrozen.oyeksfrozen.model.dto.TransactionRequest;
import com.oyeksfrozen.oyeksfrozen.model.response.PaystackResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.PaystackVerifyResponse;
import com.oyeksfrozen.oyeksfrozen.service.impl.AnotherPaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final AnotherPaystackService paystackService;

    @PostMapping("/initialize-transaction")
    public PaystackResponse initializeTransaction(@RequestBody TransactionRequest request) {
        PaystackResponse response = paystackService.initializeTransaction(request.getEmail(), request.getAmount());
        if (response != null && response.isStatus()) {
            return response;
        } else {
            return null;
        }
    }

    @GetMapping("/verify-transaction/{reference}")
    public PaystackVerifyResponse verifyTransaction(@PathVariable String reference) {
        return paystackService.verifyTransaction(reference);
    }
}
