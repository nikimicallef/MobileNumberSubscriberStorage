package com.epic.mobile.mappers;

import java.time.Instant;

import org.openapitools.model.MobileSubscription;

import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;


public class MobileSubscriptionMapper {

    /**
     * Converts from the Mobile Subscription DB model to the API Model
     *
     * @param dbModel Mobile Subscription DB entity
     * @return Mobile Subscription API Entity
     */
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

    /**
     * Converts from the Mobile Subscription API model to the DB Model
     * NOTE: The ID is NOT set if the id in the input model is null
     *
     * @param apiModel Mobile Subscription API entity
     * @return Mobile Subscription DB entity
     */
    public static MobileSubscriptionDbModel convertToDbModel(final MobileSubscription apiModel) {
        final MobileSubscriptionDbModel dbModel = new MobileSubscriptionDbModel();

        if (apiModel.getId() != null) {
            dbModel.setId(apiModel.getId());
        }

        dbModel.setMsisdn(apiModel.getMsisdn());
        dbModel.setCustomerIdOwner(apiModel.getCustomerIdOwner());
        dbModel.setCustomerIdUser(apiModel.getCustomerIdUser());
        dbModel.setServiceType(apiModel.getServiceType().getValue());

        if (apiModel.getServiceStartDate() != null) {
            dbModel.setServiceStartDate(Instant.ofEpochMilli(apiModel.getServiceStartDate()));
        }

        return dbModel;
    }
}
