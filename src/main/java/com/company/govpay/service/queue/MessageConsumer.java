package com.company.govpay.service.queue;

import com.company.govpay.domain.CheckType;
import com.company.govpay.domain.Message;
import com.company.govpay.domain.Mock;
import com.company.govpay.domain.Payment;
import com.company.govpay.service.MailService;
import com.company.govpay.service.MessageDroolService;
import com.company.govpay.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    MessagePublisher messagePublisher;

    @Autowired
    MessageDroolService droolService;

    @Autowired
    ValidationMail mail;

    private boolean valueRecieved = false;

    @JmsListener(destination = "paymentQueue")
    public void message0PercentListener(Message message) {
        // listens for 0 percent completed queue

        LOGGER.info("Message recieved: {}", message);
        message.setMessage(" needs to check name");
        droolService.insertOrUpdate(message);

        message.completeOneQueueCycle();
        messagePublisher.publishMessage(message.getNextQueue(), message);
    }

    @JmsListener(destination = "paymentQueue25Done")
    public void message25PercentListener(Message message) {
        // listens for 25 percent completed queue

        LOGGER.info("Message recieved in messageConsumer25Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage(" needs to check expiry date");
        droolService.insertOrUpdate(message);

        if (validate(message) && valueRecieved) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage aftere proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(message.getNextQueue(), message);
            valueRecieved = false;
        } else {
            if (valueRecieved) {
                mail.sendNameValidationFailureMail(message.getPayload());
                valueRecieved = false;
            } else {
                LOGGER.warn("completion halted due to some error on the request name");
            }
        }
    }

    @JmsListener(destination = "paymentQueue50Done")
    public void message50PercentListener(Message message) {
        // listens for 50 percent completed message

        LOGGER.info("Message recieved in messageConsumer50Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage(" needs to check phone number");
        droolService.insertOrUpdate(message);

        if (validate(message) && valueRecieved) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage after proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(message.getNextQueue(), message);
            valueRecieved = false;
        } else {
            if (valueRecieved) {
                mail.sendExpiryDateValidationFailureMail(message.getPayload());
                ValueRecieved = false;
            } else {
                LOGGER.warn("completion halted due to some error on the request expiry date");
            }
        }
    }

    @JmsListener(destination = "paymentQueue75Done")
    public void message75PercentListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer75Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        droolService.insertOrUpdate(message);

        if (validate(message) && valueRecieved) {
            // if it has valid name it add the percentage and pass it to the next stage

            message.completeOneQueueCycle();
            LOGGER.info(" Completion percentage after proccessed in the consumer: {}", message.getCompletionPercentage());
            messagePublisher.publishMessage(message.getNextQueue(), message);
            valueRecieved = false;
        } else {
            if (valueRecieved) {
                mail.sendPhoneValidationFailureMail(message.getPayload());
                valueRecieved = false;
            } else {
                LOGGER.warn("completion halted due to some error on the request phone");
            }
        }
    }

    @JmsListener(destination = "paymentQueue100Done")
    public void message100PercentListener(Message message) {
        LOGGER.info("Message recieved in messageConsumer100Done: {}", message);
        LOGGER.info(" Completion percentage when entered: {}", message.getCompletionPercentage());
        message.setMessage("payment completed");
        droolService.insertOrUpdate(message);

        validate(message);
        mail.sucessfullValidation(message.getPayload());
    }

    private Mock requestValidationInput(String url) {
        // requests an outer api for an input validation input

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Mock.class);
    }

    private boolean validate(Message message) {
        Mock data;

        switch (message.getCheck()) {
            case NAME:
                try {
                    data = requestValidationInput(message.getCheckURL());
                    valueRecieved = true;
                    if (message.getPayload().getName().equalsIgnoreCase(data.getText())) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            case EXPIRYDATE:
                try {
                    data = requestValidationInput(message.getCheckURL());
                    valueRecieved = true;
                    if (message.getPayload().getExpiryDate().equalsIgnoreCase(data.getText())) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            case PHONE:
                try {
                    data = requestValidationInput(message.getCheckURL());
                    valueRecieved = true;
                    if (message.getPayload().getPhoneNumber().equalsIgnoreCase(data.getText())) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            case NONE:
                return false;
            case COMPLETED:
                message.setPayload(paymentService.save(message.getPayload()));
            default:
                return false;
        }
    }
}
