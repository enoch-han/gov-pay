package com.company.govpay.web.rest;

import com.company.govpay.domain.Mock;
import com.company.govpay.domain.Payment;
import com.company.govpay.service.PaymentService;
import com.company.govpay.service.UserService;
import com.company.govpay.service.WorldLinePaymentService;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.GetHostedCheckoutResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    private final PaymentService paymentService;

    private final UserService userService;

    private final WorldLinePaymentService worldLinePaymentService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PaymentResource(PaymentService paymentService, UserService userService, WorldLinePaymentService worldLinePaymentService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.worldLinePaymentService = worldLinePaymentService;
    }

    // @GetMapping("/payments")
    // public List<Payment> getAllPayments() {
    //     log.debug("REST request to get all Payments");
    //     return paymentService.findAllByUser(userService.getUserWithAuthorities().get().getId());
    // }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", payment);
        if (payment.getId() != null) {
            return ResponseEntity
                .badRequest()
                .headers(
                    HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idexists", "A new payment cannot already have an ID")
                )
                .body(null);
        }

        System.out.println("before user id setting");
        //payment.setUserId(userService.getUserWithAuthorities().get().getId());
        System.out.println("after user id setting");
        Payment result = paymentService.save(payment);
        System.out.println("after saving data");
        return ResponseEntity
            .created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/payments/companyName")
    public Mock getCompanyName() {
        // a mock that aquires company name
        String uri = "https://mockbin.org/bin/c725e6ca-adbe-4a1a-900a-63fea1c4b760";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    @GetMapping("/payments/lastPayment")
    public Mock getLastPayment() {
        // a mock that aquires last Payment date
        String uri = "https://mockbin.org/bin/5c7b0680-ce8a-41b2-b303-2f9bd9172196";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    @PostMapping("/payments/initiate")
    public CreateHostedCheckoutResponse getInititatePayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("REST request to initiate WorldLine payment : {}", payment);

        CreateHostedCheckoutResponse response = worldLinePaymentService.initiatePayment(payment);
        worldLinePaymentService.checkoutId = response.getHostedCheckoutId();
        worldLinePaymentService.mac = response.getRETURNMAC();
        worldLinePaymentService.merchantReference = response.getMerchantReference();
        worldLinePaymentService.partialUrl = response.getPartialRedirectUrl();
        log.debug(" world line initiation response : {}", response);

        return response;
    }

    @PostMapping("/payments/getPaymentResponse")
    public GetHostedCheckoutResponse getPaymentResponse(@Valid @RequestBody String hostedCheckoutId) throws URISyntaxException {
        log.debug("REST request to get payment detail : {}", hostedCheckoutId);

        GetHostedCheckoutResponse response = worldLinePaymentService.getPaymentResponse(hostedCheckoutId);
        log.debug(" world line alues : {}", response);

        return response;
    }
}
