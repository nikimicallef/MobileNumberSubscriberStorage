package com.epic.mobile.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epic.mobile.api.models.MobileSubscriptionApiModel;
import com.epic.mobile.services.MobileSubscriptionService;


@RestController
@RequestMapping(path = "/subscriptions")
@Validated
public class SubscriptionsRestController {
    // TODO: Validation

    @Autowired
    private MobileSubscriptionService mobileSubscriptionService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MobileSubscriptionApiModel>> getAllSubscriptions() {

        final List<MobileSubscriptionApiModel> subscriptions = mobileSubscriptionService.getAllMobileSubscriptions();

        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MobileSubscriptionApiModel> createMobileSubscription(final @RequestBody MobileSubscriptionApiModel mobileSubscriptionApiModel) {
        final MobileSubscriptionApiModel mobileSubscription = mobileSubscriptionService.createMobileSubscription(mobileSubscriptionApiModel);

        return new ResponseEntity<>(mobileSubscription, HttpStatus.OK);
    }
}
