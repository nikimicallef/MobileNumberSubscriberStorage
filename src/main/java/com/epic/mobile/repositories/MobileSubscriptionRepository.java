package com.epic.mobile.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;


@Repository
public interface MobileSubscriptionRepository
        extends CrudRepository<MobileSubscriptionDbModel, Integer> {

    @Query("SELECT s FROM MOBILE_SUBSCRIPTION s WHERE (:msisdn is null or s.msisdn = :msisdn)" +
            " and (:customerIdOwner is null or s.customerIdOwner = :customerIdOwner)" +
            " and (:customerIdUser is null or s.customerIdUser = :customerIdUser)" +
            " and (:serviceType is null or s.serviceType = :serviceType)")
    List<MobileSubscriptionDbModel> findByMsisdnAndAndCustomerIdOwnerAndCustomerIdUserAndServiceType(String msisdn, Integer customerIdOwner, Integer customerIdUser, String serviceType);
}
