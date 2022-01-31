package com.company.govpay.web.rest;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.govpay.IntegrationTest;
import com.company.govpay.domain.Payment;
import com.company.govpay.repository.PaymentRepository;
import com.company.govpay.security.AuthoritiesConstants;
import com.company.govpay.service.PaymentService;
import com.company.govpay.service.WorldLinePaymentService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
public class PaymentResourceIT {

    @Autowired
    private PaymentRepository testRepository;

    @Autowired
    private WorldLinePaymentService testWorldLineService;

    @Autowired
    private PaymentService testPaymentService;

    private Payment testPayment;

    @Autowired
    private MockMvc restPaymentMock;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setName("natan");
        testPayment.setCcc("123456789");
        testPayment.setCik("12345678");
        testPayment.setCompanyName("Abc company");
        testPayment.setEmail("natan@gmail.com");
        testPayment.setPaymentAmount(Long.valueOf(12));
        testPayment.setLastPayment(Instant.EPOCH);
        testPayment.setPhoneNumber("1233456789");
        testPayment.setPaymentId("1234567898912315");
    }

    @Test
    void testIfItCreatesPaymentWithValidPayment() throws Exception {
        System.out.println("hello i am in payment resource test");
        restPaymentMock
            .perform(
                post("/api/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPayment))
                    .with(csrf())
            )
            .andExpect(status().isCreated());
    }

    @Test
    void testGetCompanyName() {}

    @Test
    void testGetInititatePayment() {}

    @Test
    void testGetLastPayment() {}

    @Test
    void testGetPaymentResponse() {}
}
