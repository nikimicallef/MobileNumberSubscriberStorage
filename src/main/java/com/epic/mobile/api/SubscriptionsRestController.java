package com.epic.mobile.api;

import java.util.List;

import org.openapitools.api.SubscriptionsApiController;
import org.openapitools.model.MobileSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import com.epic.mobile.services.MobileSubscriptionService;


@Component
public class SubscriptionsRestController
        extends SubscriptionsApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionsRestController.class);

    @Autowired
    private MobileSubscriptionService mobileSubscriptionService;

    public SubscriptionsRestController(NativeWebRequest request) {
        super(request);
    }

    @Override
    public ResponseEntity<List<MobileSubscription>> subscriptionsGet(String msisdn, Integer customerIdOwner, Integer customerIdUser, String serviceType) {
        LOGGER.info("Entered GET all subscriptions with parameters msisdn {} customerIdOwner {} customerIdUser {} serviceType {}", msisdn, customerIdOwner, customerIdUser, serviceType);

        final List<MobileSubscription> mobileSubscriptions = mobileSubscriptionService.getMobileSubscription(msisdn, customerIdOwner, customerIdUser, serviceType);

        LOGGER.info("Returning {} entities", mobileSubscriptions.size());

        return ResponseEntity.ok(mobileSubscriptions);
    }

    @Override
    public ResponseEntity<MobileSubscription> subscriptionsPost(MobileSubscription mobileSubscription) {
        LOGGER.info("Entered POST subscription with input body {}", mobileSubscription.toString());

        final MobileSubscription mobileSubscriptionResponse = mobileSubscriptionService.createMobileSubscription(mobileSubscription);

        LOGGER.info("POST subscription successful. Returning the following entity {}", mobileSubscriptionResponse.toString());

        return new ResponseEntity<>(mobileSubscriptionResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> subscriptionsIdDelete(Integer id) {
        LOGGER.info("Entered DELETE subscription with id {}", id);

        mobileSubscriptionService.deleteMobileSubscription(id);

        LOGGER.info("Entity with id {} deleted", id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MobileSubscription> subscriptionsIdGet(Integer id) {
        LOGGER.info("Entered GET subscription with id {}", id);

        final MobileSubscription mobileSubscription = mobileSubscriptionService.getMobileSubscription(id);

        LOGGER.info("Entity with id {} retrieved. Response body {}", id, mobileSubscription.toString());

        return ResponseEntity.ok(mobileSubscription);
    }

    @Override
    public ResponseEntity<MobileSubscription> subscriptionsIdPut(Integer id, MobileSubscription mobileSubscription) {
        LOGGER.info("Entered PUT subscription with id {} and request body {}", id, mobileSubscription.toString());

        final MobileSubscription updatedMobileSubscription = mobileSubscriptionService.updateMobileSubscription(id, mobileSubscription);

        LOGGER.info("Entity with id {} updated. Response body {}", id, mobileSubscription.toString());

        return ResponseEntity.ok(updatedMobileSubscription);
    }
}
