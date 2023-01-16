package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.common.communication.ActivityMatchMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GatewayActivityMatchController extends  GatewayController {

    private final transient ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter;

    /**
     * Instantiates a new Gateway Activity Match Controller.
     *
     * @param activityMatchMicroserviceAdapter activityMatchMicroserviceAdapter
     */
    @Autowired
    public GatewayActivityMatchController(ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter) {
        this.activityMatchMicroserviceAdapter = activityMatchMicroserviceAdapter;
    }

    /**
     * Endpoint for creating a new offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/create/match")
    public ResponseEntity<Void> createActivityMatch(@RequestBody MatchCreationRequestModel request,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        logger.info(String.format("Received createActivityMatch request from user: %s, for activity: %s",
                request.getUserId(), request.getActivityId()));
        return activityMatchMicroserviceAdapter.createActivityMatch(request, authToken);
    }

    /**
     * Endpoint for retrieving a list of pending offers.
     *
     * @param request the request wrapped in a PendingOffersRequestModel
     * @return a response wrapped in a PendingOffersResponseModel
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/get/offers/pending")
    public ResponseEntity<PendingOffersResponseModel> getPendingOffers(@RequestBody PendingOffersRequestModel request,
                                                                       @RequestHeader(HttpHeaders.AUTHORIZATION)
                                                                       String authToken) {
        logger.info("Received getPendingOffers request for activity: " + request.getActivityId());
        return activityMatchMicroserviceAdapter.getPendingOffers(request, authToken);
    }

    /**
     * Endpoint for setting the participant of an activity match.
     *
     * @param request the request wrapped in a SetParticipantRequestModel
     * @return a simple okay status message
     * @throws ResponseStatusException if not successful
     */
    @PostMapping("/set/participant")
    public ResponseEntity<String> setParticipant(@RequestBody SetParticipantRequestModel request,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        logger.info(String.format("Received setParticipant request for activity: %s, selected participant: %s",
                request.getActivityId(), request.getParticipantNetId()));
        return activityMatchMicroserviceAdapter.setParticipant(request, authToken);
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
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        logger.info(String.format("Received addUserToJoinQueue request for activity: %s"),
                request.getActivityId());
        return activityMatchMicroserviceAdapter.addUserToJoinQueue(request, authToken);
    }

}
