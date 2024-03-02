package com.oyeksfrozen.oyeksfrozen.service.impl;

import com.oyeksfrozen.oyeksfrozen.constants.PaystackCredentials;
import com.oyeksfrozen.oyeksfrozen.model.response.PaystackResponse;
import com.oyeksfrozen.oyeksfrozen.model.response.PaystackVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.FileNameMap;

@Service
@RequiredArgsConstructor
public class AnotherPaystackService {

    private final RestTemplate restTemplate;

    public PaystackResponse initializeTransaction(String email, int amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + PaystackCredentials.MY_PAY_STACK_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String data = "{ \"email\": \"" + email + "\", \"amount\": \"" + amount + "\", \"currency\": \"USD\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(data, headers);

        ResponseEntity<PaystackResponse> responseEntity = restTemplate.exchange(PaystackCredentials.INITIALIZATION_URL, HttpMethod.POST, requestEntity, PaystackResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            return null; // or handle error
        }
    }

    public PaystackVerifyResponse verifyTransaction(String reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + PaystackCredentials.MY_PAY_STACK_KEY);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<PaystackVerifyResponse> responseEntity = restTemplate.exchange(PaystackCredentials.VERIFY_URL + "/" + reference, HttpMethod.GET, requestEntity, PaystackVerifyResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

}
