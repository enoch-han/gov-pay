package com.company.govpay.service;

import com.company.govpay.domain.Payment;
import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.CommunicatorConfiguration;
import com.ingenico.connect.gateway.sdk.java.Factory;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.Address;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.AmountOfMoney;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutRequest;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.GetHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.definitions.HostedCheckoutSpecificInput;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Customer;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Order;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorldLinePaymentService {

    String apiKeyId = "534823c062bedc24";
    String secretApiKey = "5frGHofPzr97BppEPZNVeZiPdZlblt1VmAb1bhImsvY=";
    URL propertiesUrl = getClass().getResource("/WorldLinePayment.properties");
    String merchantId = "1070";
    String merchantName = "Merchant 1070";
    private String returnURL = "http://localhost:9000/payment-review";

    private Client client;

    private Client getClient() throws URISyntaxException {
        CommunicatorConfiguration configuration = Factory.createConfiguration(this.propertiesUrl.toURI(), apiKeyId, secretApiKey);
        return Factory.createClient(configuration);
    }

    public CreateHostedCheckoutResponse initiatePayment(Payment value) {
        CreateHostedCheckoutRequest body = new CreateHostedCheckoutRequest();
        try {
            client = getClient();
            HostedCheckoutSpecificInput hostedCheckoutSpecificInput = new HostedCheckoutSpecificInput();
            hostedCheckoutSpecificInput.setLocale("en_GB");
            hostedCheckoutSpecificInput.setVariant("100");
            hostedCheckoutSpecificInput.setReturnUrl(returnURL);

            AmountOfMoney amountOfMoney = new AmountOfMoney();
            amountOfMoney.setAmount(value.getPaymentAmount());
            amountOfMoney.setCurrencyCode("USD");

            Address billingAddress = new Address();
            billingAddress.setCountryCode("US");

            Customer customer = new Customer();
            customer.setBillingAddress(billingAddress);
            customer.setMerchantCustomerId(merchantId);

            Order order = new Order();
            order.setAmountOfMoney(amountOfMoney);
            order.setCustomer(customer);

            body.setHostedCheckoutSpecificInput(hostedCheckoutSpecificInput);
            body.setOrder(order);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return client.merchant(merchantId).hostedcheckouts().create(body);
    }

    public GetHostedCheckoutResponse getPaymentResponse(String hostedCheckoutId) {
        try {
            client = getClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client.merchant(merchantId).hostedcheckouts().get(hostedCheckoutId);
    }
}
