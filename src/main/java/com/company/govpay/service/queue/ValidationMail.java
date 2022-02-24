package com.company.govpay.service.queue;

import com.company.govpay.domain.Payment;
import com.company.govpay.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationMail {

    private String subject = "About your paygov payment";
    private String refundSnippet = "You have the right to add your complaint to our system. money will be refunded within 10days. Thanks";

    @Autowired
    MailService mailService;

    public void sendNameValidationFailureMail(Payment payment) {
        // send and email if the name validation has failed

        String to = payment.getEmail();
        String body =
            "Dear constomer your payment for paygov has been rejected due to the name you entered the name is in our blacklist." +
            refundSnippet;
        mailService.sendEmail(to, subject, body, false, false);
    }

    public void sendExpiryDateValidationFailureMail(Payment payment) {
        // sends an email if the expiry date validation has faild

        String to = payment.getEmail();
        String body =
            "Dear constomer your payment for paygov has been rejected due to the expiry date you entered the expiry date should be anything but 0222." +
            refundSnippet;
        mailService.sendEmail(to, subject, body, false, false);
    }

    public void sendPhoneValidationFailureMail(Payment payment) {
        // sends an email if the phone validation has failed

        String to = payment.getEmail();
        String body =
            "Dear constomer your payment for paygov has been rejected due to the phone number you entered the phone number should be anything but 0123456789." +
            refundSnippet;
        mailService.sendEmail(to, subject, body, false, false);
    }

    public void sucessfullValidation(Payment payment) {
        // send an email if the all the validation is sucessfull

        String to = payment.getEmail();
        String body =
            "Dear customer your payment for paygov has been successfully checked and processed." +
            "Your card payment id is ." +
            payment.getPaymentId() +
            "you can ask any transaction questions using ID: " +
            payment.getId() +
            "Thanks";
        mailService.sendEmail(to, subject, body, false, false);
    }
}
