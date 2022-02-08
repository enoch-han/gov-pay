package com.company.govpay.service.queue;

import com.company.govpay.domain.Message;
import com.company.govpay.domain.Mock;
import com.company.govpay.domain.Payment;
import com.company.govpay.service.MailService;
import com.company.govpay.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageConsumer100Done {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer100Done.class);

    @Autowired
    MailService mailService;

    @Autowired
    private PaymentService paymentService;

    @JmsListener(destination = "paymentQueue100Done")
    public void messageListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer100Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage("payment completed");
        message.setPayload(paymentService.save(message.getPayload()));
        prepareAndSendMail(message);
    }

    private void prepareAndSendMail(Message message) {
        // sends a failure message to the provided email

        String to = message.getPayload().getEmail();
        String subject = "About your paygov payment";
        String body =
            "Dear customer your payment for paygov has been successfully checked and processed." +
            "Your card payment id is ." +
            message.getPayload().getPaymentId() +
            "you can ask any transaction questions using ID: " +
            message.getPayload().getId() +
            "Thanks";
        mailService.sendEmail(to, subject, body, false, false);
    }
}
