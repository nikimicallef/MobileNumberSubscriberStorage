package com.epic.mobile.mappers;

import com.epic.mobile.api.models.ServiceTypeEnum;


public class ServiceTypeMapper {
    public static ServiceTypeEnum stringToEnum(final String serviceTypeString) {
        switch (serviceTypeString) {
            case "MOBILE_POSTPAID":
                return ServiceTypeEnum.MOBILE_POSTPAID;
            case "MOBILE_PREPAID":
                return ServiceTypeEnum.MOBILE_PREPAID;
            default:
                // TODO: Message
                throw new IllegalArgumentException("");
        }
    }
}
