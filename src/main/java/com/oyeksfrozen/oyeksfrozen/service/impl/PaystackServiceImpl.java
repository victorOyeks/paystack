package com.oyeksfrozen.oyeksfrozen.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oyeksfrozen.oyeksfrozen.PaystackPaymentRepository.AppUserRepositoryImpl;
import com.oyeksfrozen.oyeksfrozen.PaystackPaymentRepository.PaystackPaymentRepositoryImpl;
import com.oyeksfrozen.oyeksfrozen.constants.PaystackKey;
import com.oyeksfrozen.oyeksfrozen.model.domain.AppUser;
import com.oyeksfrozen.oyeksfrozen.model.domain.PaymentPaystack;
import com.oyeksfrozen.oyeksfrozen.model.dto.CreatePlanDto;
import com.oyeksfrozen.oyeksfrozen.model.dto.InitializePaymentDto;
import com.oyeksfrozen.oyeksfrozen.model.enums.PricingPlanType;
import com.oyeksfrozen.oyeksfrozen.model.response.CreatePlanResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.InitializePaymentResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.PaymentVerificationResponse;
import com.oyeksfrozen.oyeksfrozen.service.PaystackService;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import java.util.Date;

import static com.oyeksfrozen.oyeksfrozen.constants.APIConstants.*;

@Service
@RequiredArgsConstructor
public class PaystackServiceImpl implements PaystackService {

    private final PaystackPaymentRepositoryImpl paystackPaymentRepository;
    private final AppUserRepositoryImpl appUserRepository;

    @Override
    public CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception {
        CreatePlanResponse createPlanResponse = null;

        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(createPlanDto));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAYSTACK_INIT);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + PaystackKey.MY_PAY_STACK_KEY);
            post.addHeader("Cache-Control", "no-cache");

            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == STATUS_CODE_CREATED) {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to process payment at the moment " +
                        "or something wrong with request");
            }

            ObjectMapper mapper = new ObjectMapper();
            createPlanResponse = mapper.readValue(result.toString(), CreatePlanResponse.class);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        return createPlanResponse;
    }

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) {
        InitializePaymentResponse initializePaymentResponse = null;

        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(initializePaymentDto));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAYSTACK_INITIALIZE_PAY);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + PaystackKey.MY_PAY_STACK_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to initialize payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            initializePaymentResponse = mapper.readValue(result.toString(), InitializePaymentResponse.class);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        return initializePaymentResponse;
    }

    @Override
    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference, String plan, Long id) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse = null;
        PaymentPaystack paymentPaystack = null;

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(PAYSTACK_VERIFY + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + PaystackKey.MY_PAY_STACK_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;

                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to verify payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            paymentVerificationResponse = mapper.readValue(result.toString(), PaymentVerificationResponse.class);

            if (paymentVerificationResponse == null || paymentVerificationResponse.getStatus().equals("false")) {
                throw new Exception("An error");
            } else if (paymentVerificationResponse.getData().getStatus().equals("success")) {

                AppUser appUser = appUserRepository.getById(id);
                PricingPlanType pricingPlanType = PricingPlanType.valueOf(plan.toUpperCase());

                paymentPaystack = PaymentPaystack.builder()
                        .user(appUser)
                        .reference(paymentVerificationResponse.getData().getReference())
                        .amount(paymentVerificationResponse.getData().getAmount())
                        .gatewayResponse(paymentVerificationResponse.getData().getGatewayResponse())
                        .paidAt(paymentVerificationResponse.getData().getPaidAt())
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .channel(paymentVerificationResponse.getData().getChannel())
                        .currency(paymentVerificationResponse.getData().getCurrency())
                        .ipAddress(paymentVerificationResponse.getData().getIpAddress())
                        .pricingPlanType(pricingPlanType)
                        .createdOn(new Date())
                        .build();
            }
        } catch (Exception ex) {
            throw new Exception("Paystack");
        }
        paystackPaymentRepository.save(paymentPaystack);
        return paymentVerificationResponse;
    }
}
