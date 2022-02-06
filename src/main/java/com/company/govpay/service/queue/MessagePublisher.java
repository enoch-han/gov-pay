package com.company.govpay.service.queue;

import com.company.govpay.domain.Message;
import com.company.govpay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessagePublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public boolean publishMessage(Message message) {
        try {
            jmsTemplate.convertAndSend("paymentQueue", message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
