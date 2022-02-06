package com.company.govpay.service.queue;

import com.company.govpay.domain.Message;
import com.company.govpay.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private PaymentService paymentService;

    @JmsListener(destination = "paymentQueue")
    public void messageListener(Message message) {
        LOGGER.info("Message recieved: {}", message);
        paymentService.save(message.getPayload());
    }
}
