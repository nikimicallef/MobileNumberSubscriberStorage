package com.epic.mobile.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.openapitools.model.MobileSubscription;

import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


public class MobileSubscriptionMapperTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    public void convertToApiModel_validInput_equivalentOutput() {
        final String serviceType = ThreadLocalRandom.current().nextDouble() < 0.5
                ? MobileSubscription.ServiceTypeEnum.PREPAID.getValue()
                : MobileSubscription.ServiceTypeEnum.POSTPAID.getValue();

        final MobileSubscriptionDbModel dbModel = PODAM_FACTORY.manufacturePojo(MobileSubscriptionDbModel.class);
        dbModel.setServiceType(serviceType);

        final MobileSubscription apiModel = MobileSubscriptionMapper.convertToApiModel(dbModel);

        assertEquals(dbModel.getId(), apiModel.getId());
        assertEquals(dbModel.getMsisdn(), apiModel.getMsisdn());
        assertEquals(dbModel.getCustomerIdOwner(), apiModel.getCustomerIdOwner());
        assertEquals(dbModel.getCustomerIdUser(), apiModel.getCustomerIdUser());
        assertEquals(dbModel.getServiceType(), apiModel.getServiceType().getValue());
        assertEquals(dbModel.getServiceStartDate().toEpochMilli(), apiModel.getServiceStartDate());
    }

    @Test
    public void convertToDbModel_inputsNotNull_outputHasNoNullFields() {
        final MobileSubscription apiModel = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);

        final MobileSubscriptionDbModel dbModel = MobileSubscriptionMapper.convertToDbModel(apiModel);

        assertEquals(apiModel.getId(), dbModel.getId());
        assertEquals(apiModel.getMsisdn(), dbModel.getMsisdn());
        assertEquals(apiModel.getCustomerIdOwner(), dbModel.getCustomerIdOwner());
        assertEquals(apiModel.getCustomerIdUser(), dbModel.getCustomerIdUser());
        assertEquals(apiModel.getServiceType().getValue(), dbModel.getServiceType());
        assertEquals(apiModel.getServiceStartDate(), dbModel.getServiceStartDate().toEpochMilli());
    }

    @Test
    public void convertToDbModel_inputsNull_outputHasNullFields() {
        final MobileSubscription apiModel = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);
        apiModel.setId(null);
        apiModel.setServiceStartDate(null);

        final MobileSubscriptionDbModel dbModel = MobileSubscriptionMapper.convertToDbModel(apiModel);

        assertEquals(0, dbModel.getId());
        assertEquals(apiModel.getMsisdn(), dbModel.getMsisdn());
        assertEquals(apiModel.getCustomerIdOwner(), dbModel.getCustomerIdOwner());
        assertEquals(apiModel.getCustomerIdUser(), dbModel.getCustomerIdUser());
        assertEquals(apiModel.getServiceType().getValue(), dbModel.getServiceType());
        assertNull(dbModel.getServiceStartDate());
    }
}
