package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.services.ActivityMatchCreationService;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchJoiningService;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ActivityMatchJoiningController {
    protected final transient ActivityMatchCreationService activityMatchCreationService;
    protected final transient ActivityMatchJoiningService activityMatchJoiningService;
    static final Logger logger = LoggerFactory.getLogger(ActivityMatchJoiningController.class.getName());


    /**
     * Instantiates a new ActivityMatchJoiningController.
     *
     * @param activityMatchCreationService activityMatchCreationService
     * @param activityMatchJoiningService activityMatchJoiningService
     */
    @Autowired
    public ActivityMatchJoiningController(ActivityMatchCreationService activityMatchCreationService,
                                          ActivityMatchJoiningService activityMatchJoiningService) {
        this.activityMatchCreationService = activityMatchCreationService;
        this.activityMatchJoiningService = activityMatchJoiningService;
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
            activityMatchJoiningService.setParticipant(request);
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
    public ResponseEntity<String> addUserToJoinQueue(@RequestBody AddUserToJoinQueueRequestModel request,
                                                     @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken)
            throws ResponseStatusException {
        try {
            String userNetId = SecurityContextHolder.getContext().getAuthentication().getName();
            activityMatchJoiningService.addUserToJoinQueue(request, userNetId, authToken);
            return ResponseEntity.ok("Successfully added participant to activity");
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
