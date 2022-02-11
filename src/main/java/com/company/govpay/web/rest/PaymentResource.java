package com.company.govpay.web.rest;

import com.company.govpay.domain.Message;
import com.company.govpay.domain.Mock;
import com.company.govpay.domain.Payment;
import com.company.govpay.service.PaymentService;
import com.company.govpay.service.UserService;
import com.company.govpay.service.WorldLinePaymentService;
import com.company.govpay.service.queue.MessageConsumer;
import com.company.govpay.service.queue.MessagePublisher;
import com.company.govpay.web.rest.errors.BadRequestAlertException;
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

    private final MessagePublisher messagePublisher;

    // private final UserService userService;

    private final WorldLinePaymentService worldLinePaymentService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PaymentResource(
        PaymentService paymentService,
        WorldLinePaymentService worldLinePaymentService,
        MessagePublisher messagePublisher
    ) {
        this.paymentService = paymentService;
        // this.userService = userService;
        this.worldLinePaymentService = worldLinePaymentService;
        this.messagePublisher = messagePublisher;
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
            throw new BadRequestAlertException("A new payment cannot already have an ID", "paymentManagement", "idexists");
        }

        Message message = new Message();
        message.setSource("rest resource");
        message.setMessage("payment needs to validated");
        message.setPayload(payment);
        if (messagePublisher.publishMessage("paymentQueue", message)) {
            System.out.println("after payment validation started");
            return ResponseEntity
                .created(new URI("/api/payments/" + payment.getPhoneNumber()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, payment.getPhoneNumber().toString()))
                .body(payment);
        } else {
            return (ResponseEntity) ResponseEntity.badRequest();
        }
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
