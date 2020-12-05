package com.epic.mobile.api.models;

public enum ServiceTypeEnum {
    MOBILE_PREPAID("MOBILE_PREPAID"),
    MOBILE_POSTPAID("MOBILE_POSTPAID");

    private String friendlyName;

    ServiceTypeEnum(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
