package nl.tudelft.sem.template.activity.controllers;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public ResponseEntity createOffer(@RequestBody TrainingCreationRequestModel request) throws Exception {
        try {
            String position = request.getPosition();
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

    @GetMapping("/get/trainings")
    public ResponseEntity<List<ActivityOffer>> getTraining() throws Exception {
        try {
            return ResponseEntity.ok(activityOfferService.getAllTrainingOffers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
