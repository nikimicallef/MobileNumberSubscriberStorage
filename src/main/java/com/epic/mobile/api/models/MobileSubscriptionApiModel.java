package com.epic.mobile.api.models;

import java.util.Objects;

import com.sun.istack.NotNull;


public class MobileSubscriptionApiModel {
    private int id;
    @NotNull
    private String msisdn;
    @NotNull
    private Integer customerIdOwner;
    @NotNull
    private Integer customerIdUser;
    @NotNull
    private ServiceTypeEnum serviceType;
    // TODO: Epoch millis?
    private Long serviceStartDate;

    public MobileSubscriptionApiModel() {
    }

    public MobileSubscriptionApiModel(int id, String msisdn, Integer customerIdOwner, Integer customerIdUser, ServiceTypeEnum serviceType, Long serviceStartDate) {
        this.id = id;
        this.msisdn = msisdn;
        this.customerIdOwner = customerIdOwner;
        this.customerIdUser = customerIdUser;
        this.serviceType = serviceType;
        this.serviceStartDate = serviceStartDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
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
        MobileSubscriptionApiModel that = (MobileSubscriptionApiModel) o;
        return id == that.id &&
                Objects.equals(msisdn, that.msisdn) &&
                Objects.equals(customerIdOwner, that.customerIdOwner) &&
                Objects.equals(customerIdUser, that.customerIdUser) &&
                Objects.equals(serviceType, that.serviceType) &&
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
