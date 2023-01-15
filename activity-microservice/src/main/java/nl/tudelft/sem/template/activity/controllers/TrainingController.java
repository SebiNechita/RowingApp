package nl.tudelft.sem.template.activity.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.common.models.activity.TrainingCreationRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TrainingController extends ActivityOfferController {

    /**
     * Instantiates a new Training Controller.
     *
     * @param activityOfferService    activityOfferService
     * @param userMicroserviceAdapter userMicroserviceAdapter
     */
    @Autowired
    public TrainingController(ActivityOfferService activityOfferService,
                              UserMicroserviceAdapter userMicroserviceAdapter) {
        super(activityOfferService, userMicroserviceAdapter);
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
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken)
            throws Exception {
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
            logger.error(e.getMessage());
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
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken)
            throws Exception {
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
            logger.error(e.getMessage());
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
    public ResponseEntity<List<TrainingOffer>> getFilteredTrainingsForUser(
            @PathVariable("netId") NetId netId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken)
            throws ResponseStatusException {
        try {
            List<TrainingOffer> trainings = activityOfferService.getFilteredTrainings(netId, authToken).stream()
                    .filter(offer -> offer instanceof TrainingOffer)
                    .map(offer -> (TrainingOffer) offer)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
            return ResponseEntity.ok(activityOfferService.getAllTrainingOffers().stream()
                    .filter(offer -> offer instanceof TrainingOffer)
                    .map(offer -> (TrainingOffer) offer)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
