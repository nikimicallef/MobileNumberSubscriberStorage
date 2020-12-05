package com.epic.mobile.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epic.mobile.repositories.models.MobileSubscription;

@Repository
public interface MobileSubscriptionRepository extends CrudRepository<MobileSubscription, Integer> {

}
