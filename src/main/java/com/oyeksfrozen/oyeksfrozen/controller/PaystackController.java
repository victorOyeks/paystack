package com.oyeksfrozen.oyeksfrozen.controller;

import com.oyeksfrozen.oyeksfrozen.model.dto.CreatePlanDto;
import com.oyeksfrozen.oyeksfrozen.model.dto.InitializePaymentDto;
import com.oyeksfrozen.oyeksfrozen.model.response.CreatePlanResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.InitializePaymentResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.PaymentVerificationResponse;
import com.oyeksfrozen.oyeksfrozen.service.PaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paystack")
public class PaystackController {

    private final PaystackService paystackService;

    @PostMapping("/createplan")
    public CreatePlanResponse createPlan(@Validated @RequestBody CreatePlanDto createPlanDto) throws Exception {
        return paystackService.createPlan(createPlanDto);
    }

    @PostMapping("/initializepayment")
    public InitializePaymentResponse initializePayment(@Validated @RequestBody InitializePaymentDto initializePaymentDto) throws Throwable {
        return paystackService.initializePayment(initializePaymentDto);
    }

    @GetMapping("/verifypayment/{reference}/{plan}/{id}")
    public PaymentVerificationResponse paymentVerification(
            @PathVariable String reference,
            @PathVariable String plan,
            @PathVariable Long id
    ) throws Exception {
        if (reference.isEmpty() || plan.isEmpty()) {
            throw new Exception("reference, plan, and id must be provided in the path");
        }
        return paystackService.paymentVerification(reference, plan, id);
    }
}
