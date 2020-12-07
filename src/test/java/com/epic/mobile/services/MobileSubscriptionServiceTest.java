package com.epic.mobile.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.MobileSubscription;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.epic.mobile.exceptions.EpicEntityNotFoundException;
import com.epic.mobile.exceptions.EpicIncorrectRequestException;
import com.epic.mobile.repositories.MobileSubscriptionRepository;
import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(MockitoExtension.class)
public class MobileSubscriptionServiceTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Mock
    private MobileSubscriptionRepository repository;

    @InjectMocks
    private MobileSubscriptionService mobileSubscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    private void afterEach() {
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void getMobileSubscription_parametersInserted_entitiesReturned() {
        final String msisdn = PODAM_FACTORY.manufacturePojo(String.class);
        final Integer customerIdOwner = PODAM_FACTORY.manufacturePojo(Integer.class);
        final Integer customerIdUser = PODAM_FACTORY.manufacturePojo(Integer.class);
        final String serviceType = PODAM_FACTORY.manufacturePojo(String.class);

        final List<MobileSubscriptionDbModel> dbSubscriptions = PODAM_FACTORY.manufacturePojo(List.class, MobileSubscriptionDbModel.class);
        dbSubscriptions.forEach(entity -> {
            final String newServiceType = ThreadLocalRandom.current().nextDouble() < 0.5
                    ? MobileSubscription.ServiceTypeEnum.PREPAID.getValue()
                    : MobileSubscription.ServiceTypeEnum.POSTPAID.getValue();

            entity.setServiceType(newServiceType);
        });

        when(repository.findByMsisdnAndAndCustomerIdOwnerAndCustomerIdUserAndServiceType(anyString(), anyInt(), anyInt(), anyString())).thenReturn(dbSubscriptions);

        final List<MobileSubscription> returnedSubscriptions = mobileSubscriptionService.getMobileSubscription(msisdn, customerIdOwner, customerIdUser, serviceType);

        assertEquals(dbSubscriptions.size(), returnedSubscriptions.size());
        verify(repository).findByMsisdnAndAndCustomerIdOwnerAndCustomerIdUserAndServiceType(msisdn, customerIdOwner, customerIdUser, serviceType);
    }

    @Test
    public void getMobileSubscription_validId_entityReturned() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);

        final MobileSubscriptionDbModel dbEntity = generateValidMobileSubscriptionDbEntity();

        when(repository.findById(anyInt())).thenReturn(Optional.of(dbEntity));

        final MobileSubscription returnedEntity = mobileSubscriptionService.getMobileSubscription(id);

        assertNotNull(returnedEntity);
        verify(repository).findById(id);
    }

    @Test
    public void getMobileSubscription_invalidId_notFoundException() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EpicEntityNotFoundException.class, () -> mobileSubscriptionService.getMobileSubscription(id));

        verify(repository).findById(id);
    }

    @Test
    public void createMobileSubscription_validInput_entityCreated() {
        final MobileSubscription inputApiEntity = generateValidInputMobileSubscriptionApiEntity(false);

        final MobileSubscriptionDbModel dbEntity = generateValidMobileSubscriptionDbEntity();

        final ArgumentCaptor<MobileSubscriptionDbModel> argumentCaptor = ArgumentCaptor.forClass(MobileSubscriptionDbModel.class);
        when(repository.save(any(MobileSubscriptionDbModel.class))).thenReturn(dbEntity);

        final MobileSubscription returnedEntity = mobileSubscriptionService.createMobileSubscription(inputApiEntity);

        assertNotNull(returnedEntity);
        verify(repository).save(argumentCaptor.capture());

        assertEquals(1, argumentCaptor.getAllValues().size());
        final MobileSubscriptionDbModel mockParameter = argumentCaptor.getValue();

        assertEquals(inputApiEntity.getMsisdn(), mockParameter.getMsisdn());
        assertEquals(inputApiEntity.getCustomerIdOwner(), mockParameter.getCustomerIdOwner());
        assertEquals(inputApiEntity.getCustomerIdUser(), mockParameter.getCustomerIdUser());
        assertEquals(inputApiEntity.getServiceType().getValue(), mockParameter.getServiceType());
    }

    @Test
    public void createMobileSubscription_invalidInput_incorrectRequestException() {
        final MobileSubscription inputApiEntity = generateInvalidInputMobileSubscriptionApiEntity();

        EpicIncorrectRequestException exception = assertThrows(EpicIncorrectRequestException.class,
                () -> mobileSubscriptionService.createMobileSubscription(inputApiEntity));

        verify(repository, never()).save(any(MobileSubscriptionDbModel.class));

        assertEquals(5, exception.getValidationErrors().size());
    }

    @Test
    public void createMobileSubscription_nullFields_incorrectRequestException() {
        final MobileSubscription inputApiEntity = new MobileSubscription();

        EpicIncorrectRequestException exception = assertThrows(EpicIncorrectRequestException.class,
                () -> mobileSubscriptionService.createMobileSubscription(inputApiEntity));

        verify(repository, never()).save(any(MobileSubscriptionDbModel.class));

        assertEquals(4, exception.getValidationErrors().size());
    }

    @Test
    public void createMobileSubscription_validInputButDuplicateEntity_incorrectRequestException() {
        final MobileSubscription inputApiEntity = generateValidInputMobileSubscriptionApiEntity(false);

        when(repository.save(any(MobileSubscriptionDbModel.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

        assertThrows(EpicIncorrectRequestException.class, () -> mobileSubscriptionService.createMobileSubscription(inputApiEntity));

        verify(repository).save(any(MobileSubscriptionDbModel.class));
    }

    @Test
    public void deleteMobileSubscription_validId_noExceptionThrown() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);

        doNothing().when(repository).deleteById(anyInt());

        mobileSubscriptionService.deleteMobileSubscription(id);

        verify(repository).deleteById(id);
    }

    @Test
    public void deleteMobileSubscription_invalidId_notFoundException() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);

        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(anyInt());

        assertThrows(EpicEntityNotFoundException.class, () -> mobileSubscriptionService.deleteMobileSubscription(id));

        verify(repository).deleteById(id);
    }

    @Test
    public void updateMobileSubscription_validInput_entityUpdated() {
        final MobileSubscription inputApiEntity = generateValidInputMobileSubscriptionApiEntity(true);

        final MobileSubscriptionDbModel dbEntity = generateValidMobileSubscriptionDbEntity();
        dbEntity.setId(inputApiEntity.getId());
        dbEntity.setMsisdn(inputApiEntity.getMsisdn());
        dbEntity.setServiceStartDate(Instant.ofEpochMilli(inputApiEntity.getServiceStartDate()));

        when(repository.findById(anyInt())).thenReturn(Optional.of(dbEntity));

        final ArgumentCaptor<MobileSubscriptionDbModel> argumentCaptor = ArgumentCaptor.forClass(MobileSubscriptionDbModel.class);
        when(repository.save(any(MobileSubscriptionDbModel.class))).thenReturn(dbEntity);

        final MobileSubscription returnedEntity = mobileSubscriptionService.updateMobileSubscription(inputApiEntity.getId(), inputApiEntity);

        assertNotNull(returnedEntity);
        verify(repository).findById(inputApiEntity.getId());
        verify(repository).save(argumentCaptor.capture());

        assertEquals(1, argumentCaptor.getAllValues().size());
        final MobileSubscriptionDbModel mockParameter = argumentCaptor.getValue();

        assertEquals(inputApiEntity.getId(), mockParameter.getId());
        assertEquals(inputApiEntity.getMsisdn(), mockParameter.getMsisdn());
        assertEquals(inputApiEntity.getCustomerIdOwner(), mockParameter.getCustomerIdOwner());
        assertEquals(inputApiEntity.getCustomerIdUser(), mockParameter.getCustomerIdUser());
        assertEquals(inputApiEntity.getServiceType().getValue(), mockParameter.getServiceType());
        assertEquals(inputApiEntity.getServiceStartDate(), mockParameter.getServiceStartDate().toEpochMilli());
    }

    @Test
    public void updateMobileSubscription_invalidId_notFoundException() {
        final MobileSubscription inputApiEntity = generateValidInputMobileSubscriptionApiEntity(true);

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EpicEntityNotFoundException.class, () -> mobileSubscriptionService.updateMobileSubscription(inputApiEntity.getId(), inputApiEntity));

        verify(repository).findById(inputApiEntity.getId());
        verify(repository, never()).save(any(MobileSubscriptionDbModel.class));
    }

    @Test
    public void updateMobileSubscription_invalidInput_incorrectRequestException() {
        final MobileSubscription inputApiEntity = generateInvalidInputMobileSubscriptionApiEntity();

        final MobileSubscriptionDbModel dbEntity = generateValidMobileSubscriptionDbEntity();

        when(repository.findById(anyInt())).thenReturn(Optional.of(dbEntity));

        EpicIncorrectRequestException exception = assertThrows(EpicIncorrectRequestException.class,
                () -> mobileSubscriptionService.updateMobileSubscription(inputApiEntity.getId(), inputApiEntity));

        verify(repository).findById(inputApiEntity.getId());
        verify(repository, never()).save(any(MobileSubscriptionDbModel.class));

        assertEquals(5, exception.getValidationErrors().size());
    }

    @Test
    public void updateMobileSubscription_nullFields_incorrectRequestException() {
        final int inputId = 1;

        final MobileSubscription inputApiEntity = new MobileSubscription();

        final MobileSubscriptionDbModel dbEntity = generateValidMobileSubscriptionDbEntity();

        when(repository.findById(anyInt())).thenReturn(Optional.of(dbEntity));

        EpicIncorrectRequestException exception = assertThrows(EpicIncorrectRequestException.class,
                () -> mobileSubscriptionService.updateMobileSubscription(inputId, inputApiEntity));

        verify(repository).findById(inputId);
        verify(repository, never()).save(any(MobileSubscriptionDbModel.class));

        assertEquals(6, exception.getValidationErrors().size());
    }

    /**
     * Generates a valid Mobile Subscription entity for an input request
     *
     * @param generateDefaultFields if true, the id and service
     * @return the generated Mobile Subscription entity
     */
    private MobileSubscription generateValidInputMobileSubscriptionApiEntity(final boolean generateDefaultFields) {
        final MobileSubscription mobileSubscription = new MobileSubscription();
        mobileSubscription.setMsisdn(PODAM_FACTORY.manufacturePojo(Integer.class).toString());
        mobileSubscription.setCustomerIdUser(PODAM_FACTORY.manufacturePojo(Integer.class));
        mobileSubscription.setCustomerIdOwner(PODAM_FACTORY.manufacturePojo(Integer.class));
        mobileSubscription.serviceType(ThreadLocalRandom.current().nextDouble() < 0.5 ? MobileSubscription.ServiceTypeEnum.PREPAID : MobileSubscription.ServiceTypeEnum.POSTPAID);

        if (generateDefaultFields) {
            mobileSubscription.setId(PODAM_FACTORY.manufacturePojo(Integer.class));
            mobileSubscription.setServiceStartDate(PODAM_FACTORY.manufacturePojo(Long.class));
        }

        return mobileSubscription;
    }

    private MobileSubscription generateInvalidInputMobileSubscriptionApiEntity() {
        final MobileSubscription mobileSubscription = new MobileSubscription();
        mobileSubscription.setId(123);
        mobileSubscription.setMsisdn(PODAM_FACTORY.manufacturePojo(String.class));
        mobileSubscription.setCustomerIdUser(-1);
        mobileSubscription.setCustomerIdOwner(-1);
        mobileSubscription.serviceType(ThreadLocalRandom.current().nextDouble() < 0.5 ? MobileSubscription.ServiceTypeEnum.PREPAID : MobileSubscription.ServiceTypeEnum.POSTPAID);
        mobileSubscription.setServiceStartDate(123L);
        return mobileSubscription;
    }

    private MobileSubscriptionDbModel generateValidMobileSubscriptionDbEntity() {
        final MobileSubscriptionDbModel dbEntity = PODAM_FACTORY.manufacturePojo(MobileSubscriptionDbModel.class);
        final String newServiceType = ThreadLocalRandom.current().nextDouble() < 0.5
                ? MobileSubscription.ServiceTypeEnum.PREPAID.getValue()
                : MobileSubscription.ServiceTypeEnum.POSTPAID.getValue();
        dbEntity.setServiceType(newServiceType);

        return dbEntity;
    }
}
