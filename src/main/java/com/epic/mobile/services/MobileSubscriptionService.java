package com.epic.mobile.services;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openapitools.model.MobileSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.epic.mobile.exceptions.EpicEntityNotFoundException;
import com.epic.mobile.exceptions.EpicIncorrectRequestException;
import com.epic.mobile.mappers.MobileSubscriptionMapper;
import com.epic.mobile.repositories.MobileSubscriptionRepository;
import com.epic.mobile.repositories.models.MobileSubscriptionDbModel;


@Service
public class MobileSubscriptionService {

    private static final String MSISDN_REGEX = "^[1-9]\\d{1,14}$";
    private static final String DUPLICATE_MSISDN_MESSAGE = "An entity with the provided `msisdn` already exists.";
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileSubscriptionService.class);

    @Autowired
    private MobileSubscriptionRepository repository;

    /**
     * Retrieves all entities which match the provided parameters. If the parameter is null, then no match is required for the entity to be returned.
     * If all parameters are null, all entities are retrieved
     *
     * @param msisdn          field to match
     * @param customerIdOwner field to match
     * @param customerIdUser  field to match
     * @param serviceType     field to match
     * @return all entities which match the provided filtering criteria
     */
    public List<MobileSubscription> getMobileSubscription(final String msisdn, final Integer customerIdOwner, final Integer customerIdUser, final String serviceType) {
        LOGGER.debug("Entered GET all entities service method with parameters msisdn {} customerIdOwner {} customerIdUser {} serviceType {}", msisdn, customerIdOwner, customerIdUser, serviceType);

        final List<MobileSubscriptionDbModel> retrievedEntities = repository
                .findByMsisdnAndAndCustomerIdOwnerAndCustomerIdUserAndServiceType(
                        msisdn,
                        customerIdOwner,
                        customerIdUser,
                        serviceType);

        LOGGER.debug("Retrieved {} entities", retrievedEntities.size());

        return retrievedEntities.stream()
                .map(MobileSubscriptionMapper::convertToApiModel)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the mobile subscription entity for a particular id.
     * Throws an {@link EpicEntityNotFoundException} if the entity with the provided id is not found.
     *
     * @param id of the entity to retrieve
     * @return the retrieved mobile subscription entity
     */
    public MobileSubscription getMobileSubscription(final Integer id) {
        LOGGER.debug("Entered GET entity service method with id {}", id);

        final MobileSubscriptionDbModel mobileSubscriptionDbModel = retrieveMobileSubscription(id);

        return MobileSubscriptionMapper.convertToApiModel(mobileSubscriptionDbModel);
    }

    /**
     * Validates and updates the mobile subscription entity.
     * Throws an {@link EpicIncorrectRequestException} if the input body is not valid.
     *
     * @param inputApiModel with the contents of the mobile subscription entity to be created
     * @return the created mobile subscription
     */
    public MobileSubscription createMobileSubscription(final MobileSubscription inputApiModel) {

        // TODO: Thread safety/concurrency

        LOGGER.debug("Entered POST service method with body {}", inputApiModel.toString());

        final List<String> validationErrors = new ArrayList<>();

        validateInput(inputApiModel, validationErrors);

        LOGGER.debug("Input entity is valid");

        final MobileSubscriptionDbModel mobileSubscriptionDbModel = MobileSubscriptionMapper.convertToDbModel(inputApiModel, true);
        mobileSubscriptionDbModel.setServiceStartDate(Instant.now(Clock.systemUTC()));

        final MobileSubscriptionDbModel dbModel;
        try {
            dbModel = repository.save(mobileSubscriptionDbModel);
            LOGGER.debug("Entity saved in database");
        } catch (DataIntegrityViolationException e) {
            LOGGER.debug("Duplicate entity", e);
            validationErrors.add(DUPLICATE_MSISDN_MESSAGE);
            throw new EpicIncorrectRequestException(validationErrors);
        }

        return MobileSubscriptionMapper.convertToApiModel(dbModel);
    }

    /**
     * Deletes a mobile subscription for a given entity id
     *
     * @param id of the mobile subscription entity ot delete
     */
    public void deleteMobileSubscription(final Integer id) {
        LOGGER.debug("Entered DELETE service method with id {}", id);

        try {
            repository.deleteById(id);
            LOGGER.debug("Entity deleted");
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug("Entity not found");
            throw new EpicEntityNotFoundException();
        }
    }

    /**
     * Validates and updates the mobile subscription entity.
     * Throws an {@link EpicIncorrectRequestException} if the input body is not valid.
     * Throws an {@link EpicEntityNotFoundException} if the entity with the provided id is not found.
     *
     * @param id            of the mobile subscription entity to be updated
     * @param inputApiModel with the updated contents
     * @return the updated mobile subscription entity
     */
    public MobileSubscription updateMobileSubscription(final Integer id, final MobileSubscription inputApiModel) {

        // TODO: Thread safety/concurrency

        LOGGER.debug("Entered PUT service method with id {} and input model {}", id, inputApiModel.toString());

        final MobileSubscriptionDbModel mobileSubscriptionDbModel = retrieveMobileSubscription(id);

        LOGGER.debug("DB Entity for the given ID retrieved");

        final List<String> validationErrors = new ArrayList<>();

        if (!inputApiModel.getMsisdn().equals(mobileSubscriptionDbModel.getMsisdn())) {
            validationErrors.add("The `msisdn` can not be updated.");
        }

        validateInput(inputApiModel, validationErrors);

        LOGGER.debug("Input entity is valid");

        mobileSubscriptionDbModel.setMsisdn(inputApiModel.getMsisdn());
        mobileSubscriptionDbModel.setCustomerIdUser(inputApiModel.getCustomerIdUser());
        mobileSubscriptionDbModel.setCustomerIdOwner(inputApiModel.getCustomerIdOwner());
        mobileSubscriptionDbModel.setServiceType(inputApiModel.getServiceType().getValue());

        final MobileSubscriptionDbModel updatedDbModel;
        updatedDbModel = repository.save(mobileSubscriptionDbModel);

        LOGGER.debug("Entity updated");

        return MobileSubscriptionMapper.convertToApiModel(updatedDbModel);
    }

    /**
     * Retrieves a mobile subscription from the database from a particular ID.
     * Throws an {@link EpicEntityNotFoundException} if the entity with the provided id is not found.
     *
     * @param id of the mobile subscription entity
     * @return the mobile subscription entity for the given id
     */
    private MobileSubscriptionDbModel retrieveMobileSubscription(final Integer id) {
        return repository.findById(id).orElseThrow(EpicEntityNotFoundException::new);
    }

    /**
     * Validates that all the fields are not null and valid
     *
     * @param apiModel         to validate
     * @param validationErrors error to be added to this list (if required)
     */
    private void validateInput(final MobileSubscription apiModel, final List<String> validationErrors) {
        validateId(apiModel.getId(), validationErrors);

        validateMsisdn(apiModel.getMsisdn(), validationErrors);

        validateCustomerId(apiModel.getCustomerIdOwner(), validationErrors, "Owner");

        validateCustomerId(apiModel.getCustomerIdUser(), validationErrors, "User");

        if (apiModel.getServiceType() == null) {
            validationErrors.add("`serviceType` field must not be null");
        }

        validateServiceStartDate(apiModel.getServiceStartDate(), validationErrors);

        if (!validationErrors.isEmpty()) {
            LOGGER.debug("Input entity invalid");
            throw new EpicIncorrectRequestException("Incorrect data sent in the request body", validationErrors);
        }
    }

    /**
     * Validates that the serviceStartDate is
     * a) null
     *
     * @param serviceStartDate field to validate
     * @param validationErrors error to be added to this list (if required)
     */
    private void validateServiceStartDate(final Long serviceStartDate, final List<String> validationErrors) {
        if (serviceStartDate != null) {
            validationErrors.add("The `serviceStartDate` field should not be passed in");
        }
    }

    /**
     * Validates that the id is
     * a) null
     *
     * @param id               field to validate
     * @param validationErrors error to be added to this list (if required)
     */
    private void validateId(final Integer id, final List<String> validationErrors) {
        if (id != null) {
            validationErrors.add("The `id` field should not be passed in");
        }
    }

    /**
     * Validates that the customerId is
     * a) not null
     * b) greater or equal to 1
     *
     * @param customerId       field to validate
     * @param validationErrors error to be added to this list (if required)
     * @param suffix           determines whether the customer ID is os the user or the owner
     */
    private void validateCustomerId(final Integer customerId, final List<String> validationErrors, final String suffix) {
        if (customerId == null) {
            validationErrors.add("`customerId" + suffix + "` field must not be null");
        } else if (customerId <= 0) {
            validationErrors.add("`customerId" + suffix + "` must be greater or equal to 1");
        }
    }

    /**
     * Validates that the msisdn is
     * a) not null
     * b) Matches the E164 format
     *
     * @param msisdn           field to validate
     * @param validationErrors error to be added to this list (if required)
     */
    private void validateMsisdn(final String msisdn, final List<String> validationErrors) {
        if (msisdn == null) {
            // TODO: Try and remove. Also try and check null fields from the API itself.
            validationErrors.add("`msisdn` field must not be null");
        } else if (!msisdn.matches(MSISDN_REGEX)) {
            validationErrors.add("The `msisdn` must be between 2 and 15 characters long and contain only numbers");
        }
    }
}
