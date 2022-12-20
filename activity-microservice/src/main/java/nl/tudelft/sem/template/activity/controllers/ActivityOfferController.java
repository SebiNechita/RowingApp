package nl.tudelft.sem.template.activity.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.models.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.activity.models.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@RestController
public class ActivityOfferController {
    private final transient ActivityOfferService activityOfferService;

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
    public ResponseEntity createTraining(@RequestBody TrainingCreationRequestModel request,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
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
                    ownerId, boatCertificate, type, name, description, authToken);
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
    public ResponseEntity createManyTrainings(@RequestBody ManyTrainingsCreationRequestModel request,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
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
                    ownerId, boatCertificate, type, name, description, authToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for getting all trainings offer.
     *
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @GetMapping("/get/trainings")
    public ResponseEntity<List<ActivityOffer>> getTraining() throws Exception {
        try {
            return ResponseEntity.ok(activityOfferService.getAllTrainingOffers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    /**
     * Endpoint for creating a new competition offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/competition")
    public ResponseEntity createCompetition(@RequestBody CompetitionCreationRequestModel request,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
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
            String organisation = request.getOrganisation();
            boolean isFemale = request.isFemale();
            boolean isPro = request.isPro();

            activityOfferService.createCompetitionOffer(position, isActive, startTime, endTime,
                    ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro, authToken);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
