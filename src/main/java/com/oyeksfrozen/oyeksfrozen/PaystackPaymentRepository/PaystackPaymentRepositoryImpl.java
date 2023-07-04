package com.oyeksfrozen.oyeksfrozen.PaystackPaymentRepository;

import com.oyeksfrozen.oyeksfrozen.model.domain.PaymentPaystack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaystackPaymentRepositoryImpl extends JpaRepository<PaymentPaystack, Long> {
}
