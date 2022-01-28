package com.company.govpay.service;

import com.company.PayPalSvc.wsdl.DoExpressCheckoutPaymentResponseDetailsType;
import com.company.PayPalSvc.wsdl.PaymentActionCodeType;
import com.company.govpay.config.PaypalConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.SolutionTypeType;

@Service
@Transactional
public class PaypalService {

    private String returnUrl = "http://localhost:9000/payment-review";
    private String cancelUrl = "http://localhost:9000";
    private String invoiceId = "";
    Long userId = 5l;
    private String transactionId = "";
    private final Logger log = LoggerFactory.getLogger(PaypalService.class);
    private String token = "";

    private Map<String, String> configurationMap = PaypalConfiguration.getAcctAndConfig();

    public String initiatePayment(String amount) throws Exception {
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);
        SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
        SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();
        BasicAmountType amt = new BasicAmountType();
        List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
        PaymentDetailsType payDetail = new PaymentDetailsType();
        List<PaymentDetailsItemType> paymentDetails = new ArrayList<PaymentDetailsItemType>();
        PaymentDetailsItemType paymentDetail = new PaymentDetailsItemType();

        token = "";
        details.setReturnURL(returnUrl);
        details.setCancelURL(cancelUrl);
        invoiceId = "INVOICE-" + Math.random();
        details.setInvoiceID(invoiceId);
        amt.setValue(amount);
        amt.setCurrencyID(CurrencyCodeType.USD);
        log.debug("#############################  {} #####################", amt.getValue());
        paymentDetail.setAmount(amt);
        paymentDetails.add(paymentDetail);
        payDetail.setPaymentDetailsItem(paymentDetails);
        payDetails.add(payDetail);
        details.setPaymentDetails(payDetails);
        details.setOrderTotal(amt);
        details.setSolutionType(SolutionTypeType.MARK);
        setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

        SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
        expressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutReq);

        SetExpressCheckoutResponseType setExpressCheckoutResponse = service.setExpressCheckout(expressCheckoutReq);
        if (setExpressCheckoutResponse != null) {
            if (setExpressCheckoutResponse.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                token = setExpressCheckoutResponse.getToken();
            }
        }
        return token;
    }

    public String getTransactionId(String token) throws Exception {
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);
        GetExpressCheckoutDetailsReq req = new GetExpressCheckoutDetailsReq();
        GetExpressCheckoutDetailsRequestType reqType = new GetExpressCheckoutDetailsRequestType(token);
        req.setGetExpressCheckoutDetailsRequest(reqType);
        GetExpressCheckoutDetailsResponseType resp = service.getExpressCheckoutDetails(req);
        if (resp != null) {
            if (resp.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                DoExpressCheckoutPaymentRequestType doCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
                DoExpressCheckoutPaymentRequestDetailsType doDetails = new DoExpressCheckoutPaymentRequestDetailsType();
                doDetails.setToken(token);
                doDetails.setPayerID(resp.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID());
                doDetails.setPaymentDetails(resp.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails());
                doCheckoutPaymentRequestType.setDoExpressCheckoutPaymentRequestDetails(doDetails);
                DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
                doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doCheckoutPaymentRequestType);
                DoExpressCheckoutPaymentResponseType doCheckoutPaymentResponseType = service.doExpressCheckoutPayment(
                    doExpressCheckoutPaymentReq
                );
                if (doCheckoutPaymentResponseType != null) {
                    if (doCheckoutPaymentResponseType.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                        this.transactionId =
                            doCheckoutPaymentResponseType
                                .getDoExpressCheckoutPaymentResponseDetails()
                                .getPaymentInfo()
                                .get(doCheckoutPaymentResponseType.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().size() - 1)
                                .getTransactionID();
                    }
                }
            }
        }
        return this.transactionId;
    }
}
