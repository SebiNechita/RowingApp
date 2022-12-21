package nl.tudelft.sem.template.activity.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.models.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ActivityOfferController {
    private final transient ActivityOfferService activityOfferService;
    static final Logger logger = LoggerFactory.getLogger(ActivityOfferController.class.getName());

    /**
     * Instantiates a new ActivityOfferController.
     *
     * @param activityOfferService activityOfferService
     */
    @Autowired
    public ActivityOfferController(ActivityOfferService activityOfferService) {
        this.activityOfferService = activityOfferService;
    }

    /**
     * Endpoint for creating a new offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/training")
    public ResponseEntity createOffer(@RequestBody TrainingCreationRequestModel request)
            throws ResponseStatusException {
        try {
            TypesOfPositions position = request.getPosition();
            boolean isActive = request.isActive();
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = request.getEndTime();
            String ownerId = request.getOwnerId();
            String boatCertificate = request.getBoatCertificate();
            TypesOfActivities type = request.getType();
            String name = request.getName();
            String description = request.getDescription();

            activityOfferService.createTrainingOffer(position, isActive, startTime, endTime,
                    ownerId, boatCertificate, type, name, description);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for creating multiple new offers.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/training/many")
    public ResponseEntity createManyOffers(@RequestBody ManyTrainingsCreationRequestModel request)
            throws ResponseStatusException {
        try {
            Map<TypesOfPositions, Integer> positions = request.getPositions();
            boolean isActive = request.isActive();
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = request.getEndTime();
            String ownerId = request.getOwnerId();
            String boatCertificate = request.getBoatCertificate();
            TypesOfActivities type = request.getType();
            String name = request.getName();
            String description = request.getDescription();

            activityOfferService.createManyTrainingOffers(positions, isActive, startTime, endTime,
                    ownerId, boatCertificate, type, name, description);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for getting all trainings offer that are good according to the user availability.
     *
     * @return ok response if successful
     * @throws ResponseStatusException if not successful
     */
    @GetMapping("/get/trainings/{netId}")
    public ResponseEntity<List<TrainingOffer>> getFilteredOffersForUser(@PathVariable("netId") NetId netId) throws ResponseStatusException {
        try {
            List<TrainingOffer> trainings = activityOfferService.getFilteredOffers().stream()
                    .filter(offer -> offer instanceof TrainingOffer)
                    .map(offer -> (TrainingOffer) offer)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for getting all trainings offer.
     *
     * @return ok response if successful
     * @throws ResponseStatusException if not successful
     */
    @GetMapping("/get/trainings")
    public ResponseEntity<List<ActivityOffer>> getTraining() throws ResponseStatusException {
        try {
            return ResponseEntity.ok(activityOfferService.getAllTrainingOffers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for checking if a participant is eligible to join a given activity.
     *
     * @param request wrapped in a ParticipantIsEligibleRequestModel.
     * @return boolean indicating eligibility.
     * @throws ResponseStatusException if not successful.
     */
    @PostMapping("/competition/participant-is-eligible")
    public ResponseEntity<Boolean> participantIsEligible(@RequestBody ParticipantIsEligibleRequestModel request)
            throws ResponseStatusException {
        try {
            return ResponseEntity.ok(activityOfferService.participantIsEligible(request));
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
