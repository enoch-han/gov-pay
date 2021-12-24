package com.company.govpay.web.rest;

import com.company.govpay.domain.Payment;
import com.company.govpay.service.PaymentService;
import com.company.govpay.service.UserService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    private final PaymentService paymentService;

    private final UserService userService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PaymentResource(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentService.findAllByUser(userService.getUserWithAuthorities().get().getId());
    }

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
        payment.setUserId(userService.getUserWithAuthorities().get().getId());
        System.out.println("after user id setting");
        Payment result = paymentService.save(payment);
        System.out.println("after saving data");
        return ResponseEntity
            .created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
