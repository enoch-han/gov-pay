package com.company.govpay.config;

import java.util.HashMap;
import java.util.Map;

public class PaypalConfiguration {

    // Creates a configuration map containing credentials and other required configuration parameters.
    public static final Map<String, String> getAcctAndConfig() {
        Map<String, String> configMap = new HashMap<String, String>();
        configMap.putAll(getConfig());

        // Account Credential
        configMap.put("acct1.UserName", "sb-okung11027868_api1.business.example.com");
        configMap.put("acct1.Password", "5H6QLTK2PCZHBPKR");
        configMap.put("acct1.Signature", "A6P0mzeHGniAjAXRQAXxrIyMSG-rAwAH7fqi.dQX-vYkQp2hCylCEn7e");

        return configMap;
    }

    public static final Map<String, String> getConfig() {
        Map<String, String> configMap = new HashMap<String, String>();

        configMap.put("mode", "sandbox");

        return configMap;
    }
}
