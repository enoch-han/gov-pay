package com.company.govpay.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.company.govpay.IntegrationTest;
import com.company.govpay.domain.Payment;
import com.ingenico.connect.gateway.sdk.java.ReferenceException;
import com.ingenico.connect.gateway.sdk.java.ValidationException;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.GetHostedCheckoutResponse;
import java.net.URL;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class WorldLinePaymentServiceIT {

    private Payment testPayment;

    private WorldLinePaymentService worldLinePaymentService;

    private String testHostedCheckoutUnprocessedId = "061f6ff1-3b40-71ff-94fe-9b2dd6e229cd";
    private String testHostedCheckoutInvalidId = "061f6ff1-3b40-71ff-94fe-9b2dd12329cd";
    private String testHostedCheckoutValidId = "061fbdeb-ce14-71ff-901d-5540f82e3ad6";
    private String partialUrl =
        "pay1.sandbox.secured-by-ingenico.com/checkout/1070-ec83322a415c4cdb84e9ac92b6a24399:061fbdeb-ce14-71ff-901d-5540f82e3ad6:a22c5034db314c55868b27a77ef26e71";

    @BeforeEach
    public void init() {
        worldLinePaymentService = new WorldLinePaymentService();
        testPayment = new Payment();
        testPayment.setName("natan");
        testPayment.setCcc("123456789");
        testPayment.setCik("12345678");
        testPayment.setCompanyName("Abc company");
        testPayment.setEmail("natan@gmail.com");
        testPayment.setPaymentAmount(Long.valueOf(12));
        testPayment.setLastPayment(Instant.EPOCH);
        testPayment.setPhoneNumber("1233456789");
    }

    @Test
    void testIfInitiatesPaymentSuccessfullyWithCorrectPayment() {
        //test the response of initiate payment with correct payment
        CreateHostedCheckoutResponse testResponse = worldLinePaymentService.initiatePayment(testPayment);
        System.out.println(testResponse.getHostedCheckoutId());
        System.out.println(testResponse.getPartialRedirectUrl());
        //System.out.println(worldLinePaymentService.initiatePayment(testPayment).getHostedCheckoutId());
        assertThat(worldLinePaymentService.initiatePayment(testPayment)).isInstanceOf(CreateHostedCheckoutResponse.class);
    }

    @Test
    void testIfInitiatePaymentFailsWithError() {
        //test the response of initiate payment with incorrect payment
        assertThatThrownBy(() -> worldLinePaymentService.initiatePayment(new Payment())).isInstanceOf(ValidationException.class);
    }

    @Test
    void testGetPaymentResponseWithValidId() {
        //test the response get payment response with a valid id
        assertThat(worldLinePaymentService.getPaymentResponse(testHostedCheckoutValidId)).isInstanceOf(GetHostedCheckoutResponse.class);
    }

    @Test
    void testGetPaymentResponseWithInvalidId() {
        //test the response get payment response with a invalid id
        assertThatThrownBy(() -> worldLinePaymentService.getPaymentResponse(testHostedCheckoutInvalidId))
            .isInstanceOf(ReferenceException.class);
    }
}
