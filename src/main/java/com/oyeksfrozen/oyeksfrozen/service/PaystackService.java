package com.oyeksfrozen.oyeksfrozen.service;

import com.oyeksfrozen.oyeksfrozen.model.dto.CreatePlanDto;
import com.oyeksfrozen.oyeksfrozen.model.dto.InitializePaymentDto;
import com.oyeksfrozen.oyeksfrozen.model.response.CreatePlanResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.InitializePaymentResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.PaymentVerificationResponse;

public interface PaystackService {
    CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception;
    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
    PaymentVerificationResponse paymentVerification(String reference, String plan, Long id) throws Exception;
}
