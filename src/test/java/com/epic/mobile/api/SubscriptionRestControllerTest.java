package com.epic.mobile.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.MobileSubscription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.epic.mobile.services.MobileSubscriptionService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(MockitoExtension.class)
public class SubscriptionRestControllerTest {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Mock
    private MobileSubscriptionService service;

    @InjectMocks
    private SubscriptionsRestController subscriptionsRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    private void afterEach() {
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    public void subscriptionsGet_parametersInserted_200WithEntitiesReturned() {
        final String msisdn = PODAM_FACTORY.manufacturePojo(String.class);
        final Integer customerIdOwner = PODAM_FACTORY.manufacturePojo(Integer.class);
        final Integer customerIdUser = PODAM_FACTORY.manufacturePojo(Integer.class);
        final String serviceType = PODAM_FACTORY.manufacturePojo(String.class);

        final List<MobileSubscription> returnedSubscriptions = PODAM_FACTORY.manufacturePojo(List.class, MobileSubscription.class);

        when(service.getMobileSubscription(anyString(), anyInt(), anyInt(), anyString())).thenReturn(returnedSubscriptions);

        final ResponseEntity<List<MobileSubscription>> responseEntity = subscriptionsRestController.subscriptionsGet(msisdn, customerIdOwner, customerIdUser, serviceType);

        assertEquals(returnedSubscriptions, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(service).getMobileSubscription(msisdn, customerIdOwner, customerIdUser, serviceType);
    }

    @Test
    public void subscriptionsPost_validInputEntity_201WithEntityReturned() {
        final MobileSubscription inputSubscription = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);
        final MobileSubscription outputSubscription = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);

        when(service.createMobileSubscription(any(MobileSubscription.class))).thenReturn(outputSubscription);

        final ResponseEntity<MobileSubscription> responseEntity = subscriptionsRestController.subscriptionsPost(inputSubscription);

        assertEquals(outputSubscription, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(service).createMobileSubscription(inputSubscription);
    }

    @Test
    public void subscriptionsIdDelete_validIdInput_204WithNoBodyReturned() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);

        doNothing().when(service).deleteMobileSubscription(anyInt());

        final ResponseEntity<Void> responseEntity = subscriptionsRestController.subscriptionsIdDelete(id);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(service).deleteMobileSubscription(id);
    }

    @Test
    public void subscriptionsIdGet_validIdInput_200WithEntityReturned() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);
        final MobileSubscription outputSubscription = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);

        when(service.getMobileSubscription(anyInt())).thenReturn(outputSubscription);

        final ResponseEntity<MobileSubscription> responseEntity = subscriptionsRestController.subscriptionsIdGet(id);

        assertEquals(outputSubscription, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(service).getMobileSubscription(id);
    }

    @Test
    public void subscriptionsIdPut_validInput_200WithUpdatedEntityReturned() {
        final Integer id = PODAM_FACTORY.manufacturePojo(Integer.class);
        final MobileSubscription inputSubscription = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);
        final MobileSubscription outputSubscription = PODAM_FACTORY.manufacturePojo(MobileSubscription.class);

        when(service.updateMobileSubscription(anyInt(), any(MobileSubscription.class))).thenReturn(outputSubscription);

        final ResponseEntity<MobileSubscription> responseEntity = subscriptionsRestController.subscriptionsIdPut(id, inputSubscription);

        assertEquals(outputSubscription, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(service).updateMobileSubscription(id, inputSubscription);
    }
}
