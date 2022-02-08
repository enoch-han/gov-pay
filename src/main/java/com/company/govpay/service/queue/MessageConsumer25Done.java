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
public class MessageConsumer25Done {

    // this consumer validates the payer it be anyone either than nat

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer25Done.class);

    @Autowired
    MailService mailService;

    @Autowired
    MessagePublisher messagePublisher;

    private String nextQueue = "paymentQueue50Done";
    private boolean nameRecived = false;

    @JmsListener(destination = "paymentQueue25Done")
    public void messageListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer25Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage(" needs to check expiry date");
        if (nameChecker(message.getPayload()) && nameRecived) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage aftere proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(nextQueue, message);
        } else {
            if (nameRecived) {
                prepareAndSendMail(message);
            } else {
                LOGGER.warn("completion halted due to some error on the request name");
            }
        }
    }

    private Mock requestName() {
        String uri = "https://mockbin.org/bin/6dfdc74c-749f-4ea0-935e-ed9c9d6650e1";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    private boolean nameChecker(Payment payment) {
        Mock data;
        try {
            data = requestName();
            nameRecived = true;
            if (payment.getName().equalsIgnoreCase(data.getText())) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void prepareAndSendMail(Message message) {
        // sends a failure message to the provided email

        String to = message.getPayload().getEmail();
        String subject = "About your paygov payment";
        String body =
            "Dear constomer your payment for paygov has been rejected due to the name you entered the name is in our blacklist." +
            "You have the right to add your complaint to our system. money will be refunded within 10days." +
            "Thanks";
        mailService.sendEmail(to, subject, body, false, false);
    }
}
