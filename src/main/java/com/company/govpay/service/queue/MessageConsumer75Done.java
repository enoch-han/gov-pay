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
public class MessageConsumer75Done {

    // this consumer validates if the currency type is not EUR

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer75Done.class);

    @Autowired
    MailService mailService;

    @Autowired
    MessagePublisher messagePublisher;

    private String nextQueue = "paymentQueue100Done";
    private boolean phoneRecieved = false;

    @JmsListener(destination = "paymentQueue75Done")
    public void messageListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer75Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        if (phoneChecker(message.getPayload()) && phoneRecieved) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage after proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(nextQueue, message);
        } else {
            if (phoneRecieved) {
                prepareAndSendMail(message);
            } else {
                LOGGER.warn("completion halted due to some error on the request phone");
            }
        }
    }

    private Mock requestPhone() {
        String uri = "https://mockbin.org/bin/861021e7-5859-4f9b-8157-1861e23035c0";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    private boolean phoneChecker(Payment payment) {
        Mock data;
        try {
            data = requestPhone();
            phoneRecieved = true;
            if (payment.getPhoneNumber().equalsIgnoreCase(data.getText())) {
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
            "Dear constomer your payment for paygov has been rejected due to the phone number you entered the phone number should be anything but 0123456789." +
            "You have the right to add your complaint to our system. money will be refunded within 10days." +
            "Thanks";
        mailService.sendEmail(to, subject, body, false, false);
    }
}
