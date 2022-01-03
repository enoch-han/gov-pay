package com.company.govpay.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "payment_Id")
    private String paymentId;

    @NotNull
    @Column(name = "cik", nullable = false)
    private String cik;

    @NotNull
    @Column(name = "ccc", nullable = false)
    private String ccc;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "last_payment")
    private Instant lastPayment = null;

    @NotNull
    @Column(name = "payment_amount", nullable = false)
    private Long paymentAmount;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCik() {
        return cik;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Instant getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Instant lastPayment) {
        this.lastPayment = lastPayment;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ccc == null) ? 0 : ccc.hashCode());
        result = prime * result + ((cik == null) ? 0 : cik.hashCode());
        result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastPayment == null) ? 0 : lastPayment.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((paymentAmount == null) ? 0 : paymentAmount.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Payment other = (Payment) obj;
        if (ccc == null) {
            if (other.ccc != null) return false;
        } else if (!ccc.equals(other.ccc)) return false;
        if (cik == null) {
            if (other.cik != null) return false;
        } else if (!cik.equals(other.cik)) return false;
        if (companyName == null) {
            if (other.companyName != null) return false;
        } else if (!companyName.equals(other.companyName)) return false;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (lastPayment == null) {
            if (other.lastPayment != null) return false;
        } else if (!lastPayment.equals(other.lastPayment)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (paymentAmount == null) {
            if (other.paymentAmount != null) return false;
        } else if (!paymentAmount.equals(other.paymentAmount)) return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null) return false;
        } else if (!phoneNumber.equals(other.phoneNumber)) return false;
        return true;
    }

    @Override
    public String toString() {
        return (
            "Payment [ccc=" +
            ccc +
            ", cik=" +
            cik +
            ", companyName=" +
            companyName +
            ", email=" +
            email +
            ", id=" +
            id +
            ", lastPayment=" +
            lastPayment +
            ", name=" +
            name +
            ", paymentAmount=" +
            paymentAmount +
            ", phoneNumber=" +
            phoneNumber +
            "]"
        );
    }
}
