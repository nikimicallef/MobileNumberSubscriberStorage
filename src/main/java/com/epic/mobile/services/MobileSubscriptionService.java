package com.epic.mobile.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epic.mobile.api.models.MobileSubscriptionApiModel;
import com.epic.mobile.mappers.MobileSubscriptionMapper;
import com.epic.mobile.repositories.MobileSubscriptionRepository;
import com.epic.mobile.repositories.models.MobileSubscription;

@Service
public class MobileSubscriptionService {

    @Autowired
    private MobileSubscriptionRepository repository;

    public List<MobileSubscriptionApiModel> getAllMobileSubscriptions() {
        final Iterable<MobileSubscription> mobileSubscriptions = repository.findAll();

        return StreamSupport.stream(mobileSubscriptions.spliterator(), false)
                .map(MobileSubscriptionMapper::convertToApiModel)
                .collect(Collectors.toList());
    }

    public MobileSubscriptionApiModel createMobileSubscription(final MobileSubscriptionApiModel apiModel) {

        // TODO: Validate Telephone number
        // TODO: Validate entity with Phone No. doesn't exist
        // TODO: Thread safety/concurrency

        final MobileSubscription mobileSubscription = MobileSubscriptionMapper.convertToDbModel(apiModel, true);
        mobileSubscription.setServiceStartDate(Instant.now().toEpochMilli());

        final MobileSubscription dbModel = repository.save(mobileSubscription);

        return MobileSubscriptionMapper.convertToApiModel(dbModel);
    }
}
