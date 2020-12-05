package com.epic.mobile.repositories.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class MobileSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String msisdn;
    private Integer customerIdOwner;
    private Integer customerIdUser;
    private String serviceType;
    // TODO: Epoch millis?
    private Long serviceStartDate;

    public MobileSubscription() {
    }

    public MobileSubscription(String msisdn, Integer customerIdOwner, Integer customerIdUser, String serviceType) {
        this.msisdn = msisdn;
        this.customerIdOwner = customerIdOwner;
        this.customerIdUser = customerIdUser;
        this.serviceType = serviceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getCustomerIdOwner() {
        return customerIdOwner;
    }

    public void setCustomerIdOwner(Integer customerIdOwner) {
        this.customerIdOwner = customerIdOwner;
    }

    public Integer getCustomerIdUser() {
        return customerIdUser;
    }

    public void setCustomerIdUser(Integer customerIdUser) {
        this.customerIdUser = customerIdUser;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(Long serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MobileSubscription that = (MobileSubscription) o;
        return Objects.equals(id, that.id) &&
                msisdn.equals(that.msisdn) &&
                customerIdOwner.equals(that.customerIdOwner) &&
                customerIdUser.equals(that.customerIdUser) &&
                serviceType.equals(that.serviceType) &&
                Objects.equals(serviceStartDate, that.serviceStartDate);
    }

    @Override public int hashCode() {
        return Objects.hash(id, msisdn, customerIdOwner, customerIdUser, serviceType, serviceStartDate);
    }

    @Override public String toString() {
        return "MobileSubscriptionModel{" +
                "id=" + id +
                ", msisdn='" + msisdn + '\'' +
                ", customerIdOwner=" + customerIdOwner +
                ", customerIdUser=" + customerIdUser +
                ", serviceType='" + serviceType + '\'' +
                ", serviceStartDate=" + serviceStartDate +
                '}';
    }
}
