package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ActivityMatchController {
    private final transient ActivityMatchService activityMatchService;
    static final Logger logger = LoggerFactory.getLogger(ActivityMatchController.class.getName());

    /**
     * Instantiates a new ActivityMatchController.
     *
     * @param activityMatchService activityMatchService
     */
    @Autowired
    public ActivityMatchController(ActivityMatchService activityMatchService) {
        this.activityMatchService = activityMatchService;
    }

    /**
     * Endpoint for creating a new offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/create/match")
    public ResponseEntity createActivityMatch(@RequestBody MatchCreationRequestModel request)
            throws ResponseStatusException {
        try {
            activityMatchService.createActivityMatch(request);
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for retrieving a list of pending offers.
     *
     * @param request the request wrapped in a PendingOffersRequestModel
     * @return a response wrapped in a PendingOffersResponseModel
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/get/offers/pending")
    public ResponseEntity<PendingOffersResponseModel> getPendingOffers(@RequestBody PendingOffersRequestModel request)
            throws ResponseStatusException {
        try {
            return ResponseEntity.ok(activityMatchService.getPendingOffers(request));
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint for setting the participant of an activity match.
     *
     * @param request the request wrapped in a SetParticipantRequestModel
     * @return a simple okay status message
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/set/participant")
    public ResponseEntity<String> setParticipant(@RequestBody SetParticipantRequestModel request)
            throws ResponseStatusException {
        try {
            String ownerNetId = SecurityContextHolder.getContext().getAuthentication().getName();
            activityMatchService.setParticipant(request, ownerNetId);
            return ResponseEntity.ok("Successfully set participant");
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Adds a user to the join queue of an activity.
     *
     * @param request the request wrapped in an AddUserToJoinQueueRequestModel
     * @return a simple okay status message
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/join-queue")
    public ResponseEntity<String> addUserToJoinQueue(@RequestBody AddUserToJoinQueueRequestModel request)
            throws ResponseStatusException {
        try {
            String userNetId = SecurityContextHolder.getContext().getAuthentication().getName();
            activityMatchService.addUserToJoinQueue(request, userNetId);
            return ResponseEntity.ok("Successfully added participant to activity");
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
