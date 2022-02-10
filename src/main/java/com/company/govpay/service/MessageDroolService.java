package com.company.govpay.service;

import com.company.govpay.domain.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageDroolService {

    private final String nameCheckURL = "https://mockbin.org/bin/6dfdc74c-749f-4ea0-935e-ed9c9d6650e1";
    private final String expiryDateCheckURL = "https://mockbin.org/bin/27e9e382-f4ef-4525-a38e-c8e86d2887fc";
    private final String phoneCheckURL = "https://mockbin.org/bin/861021e7-5859-4f9b-8157-1861e23035c0";
    private final String queueFor25 = "paymentQueue25Done";
    private final String queueFor50 = "paymentQueue50Done";
    private final String queueFor75 = "paymentQueue75Done";
    private final String queueFor100 = "paymentQueue100Done";

    private KieSession session;

    private KieContainer container;

    public MessageDroolService(KieContainer container) {
        this.container = container;
        this.session = createSession();
    }

    public KieSession createSession() {
        KieSession ksession = container.newKieSession();
        ksession.setGlobal("nameCheckURL", nameCheckURL);
        ksession.setGlobal("expiryDateCheckURL", expiryDateCheckURL);
        ksession.setGlobal("phoneCheckURL", phoneCheckURL);
        ksession.setGlobal("queueFor25", queueFor25);
        ksession.setGlobal("queueFor50", queueFor50);
        ksession.setGlobal("queueFor75", queueFor75);
        ksession.setGlobal("queueFor100", queueFor100);
        return ksession;
    }

    public void insertOrUpdate(Message message) {
        if (message == null) {
            return;
        }
        FactHandle factHandle = session.getFactHandle(message);
        if (factHandle == null) {
            session.insert(message);
            session.fireAllRules();
        } else {
            session.update(factHandle, message);
            session.fireAllRules();
        }
    }

    public void executeRules() {
        session.fireAllRules();
    }

    public void terminate() {
        session.dispose();
    }
}
