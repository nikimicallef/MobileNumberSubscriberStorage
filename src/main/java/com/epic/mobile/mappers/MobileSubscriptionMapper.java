package com.epic.mobile.mappers;

import java.time.Instant;

import org.openapitools.model.MobileSubscription;

import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;


public class MobileSubscriptionMapper {

    // TODO: Object Mapper

    public static MobileSubscription convertToApiModel(final MobileSubscriptionDbModel dbModel) {

        final MobileSubscription apiModel = new MobileSubscription();
        apiModel.setId(dbModel.getId());
        apiModel.setMsisdn(dbModel.getMsisdn());
        apiModel.setCustomerIdOwner(dbModel.getCustomerIdOwner());
        apiModel.setCustomerIdUser(dbModel.getCustomerIdUser());
        final MobileSubscription.ServiceTypeEnum serviceTypeEnum = MobileSubscription.ServiceTypeEnum.fromValue(dbModel.getServiceType());
        apiModel.serviceType(serviceTypeEnum);
        apiModel.setServiceStartDate(dbModel.getServiceStartDate().toEpochMilli());

        return apiModel;
    }

    public static MobileSubscriptionDbModel convertToDbModel(final MobileSubscription apiModel, final boolean nullFields) {
        final MobileSubscriptionDbModel dbModel = new MobileSubscriptionDbModel();
        dbModel.setMsisdn(apiModel.getMsisdn());
        dbModel.setCustomerIdOwner(apiModel.getCustomerIdOwner());
        dbModel.setCustomerIdUser(apiModel.getCustomerIdUser());
        dbModel.setServiceType(apiModel.getServiceType().getValue());

        if (!nullFields) {
            dbModel.setId(apiModel.getId());
            dbModel.setServiceStartDate(Instant.ofEpochMilli(apiModel.getServiceStartDate()));
        }

        return dbModel;
    }
}
