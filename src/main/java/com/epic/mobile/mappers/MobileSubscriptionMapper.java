package com.epic.mobile.mappers;

import com.epic.mobile.api.models.MobileSubscriptionApiModel;
import com.epic.mobile.api.models.ServiceTypeEnum;
import com.epic.mobile.repositories.models.MobileSubscription;


public class MobileSubscriptionMapper {
    public static MobileSubscriptionApiModel convertToApiModel(final MobileSubscription dbModel) {
        final ServiceTypeEnum serviceTypeEnum = ServiceTypeMapper.stringToEnum(dbModel.getServiceType());

        return new MobileSubscriptionApiModel(dbModel.getId(),
                dbModel.getMsisdn(),
                dbModel.getCustomerIdOwner(),
                dbModel.getCustomerIdUser(),
                serviceTypeEnum,
                dbModel.getServiceStartDate() );
    }

    public static MobileSubscription convertToDbModel(final MobileSubscriptionApiModel apiModel, final boolean nullFields) {
        return new MobileSubscription(
                apiModel.getMsisdn(),
                apiModel.getCustomerIdOwner(),
                apiModel.getCustomerIdUser(),
                apiModel.getServiceType().getFriendlyName());
    }
}
