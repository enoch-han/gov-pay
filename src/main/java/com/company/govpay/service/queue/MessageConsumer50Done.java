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
public class MessageConsumer50Done {

    // this consumer validates if the expiry date is anything other than 0222

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer50Done.class);

    @Autowired
    MailService mailService;

    @Autowired
    MessagePublisher messagePublisher;

    private String nextQueue = "paymentQueue75Done";
    private boolean expiryDateRecieved = false;

    @JmsListener(destination = "paymentQueue50Done")
    public void messageListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer50Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage(" needs to check phone number");
        if (expiryDateChecker(message.getPayload()) && expiryDateRecieved) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage after proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(nextQueue, message);
        } else {
            if (expiryDateRecieved) {
                prepareAndSendMail(message);
            } else {
                LOGGER.warn("completion halted due to some error on the request expiry date");
            }
        }
    }

    private Mock requestExpiryDate() {
        String uri = "https://mockbin.org/bin/27e9e382-f4ef-4525-a38e-c8e86d2887fc";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    private boolean expiryDateChecker(Payment payment) {
        Mock data;
        try {
            data = requestExpiryDate();
            expiryDateRecieved = true;
            if (payment.getExpiryDate().equalsIgnoreCase(data.getText())) {
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
            "Dear constomer your payment for paygov has been rejected due to the expiry date you entered the expiry date should be anything but 0222." +
            "You have the right to add your complaint to our system. money will be refunded within 10days." +
            "Thanks";
        mailService.sendEmail(to, subject, body, false, false);
    }
}
