package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activitymatch.*;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationRequestModel;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationResponseModel;
import nl.tudelft.sem.template.common.models.authentication.RegistrationRequestModel;
import nl.tudelft.sem.template.gateway.communication.ActivityMatchMicroserviceAdapter;
import nl.tudelft.sem.template.gateway.communication.AuthenticationMicroserviceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Gateway controller.
 */
@RestController
public class GatewayController {

    private final transient AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter;
    private final transient ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter;
    static final Logger logger = LoggerFactory.getLogger(GatewayController.class.getName());

    /**
     * Instantiates a new GatewayController.
     */
    @Autowired
    public GatewayController(AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter,
                             ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter) {

        this.authenticationMicroserviceAdapter = authenticationMicroserviceAdapter;
        this.activityMatchMicroserviceAdapter = activityMatchMicroserviceAdapter;
    }

    /**
     * Gateway endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        logger.info("Received authentication request from user: " + request.getNetId());
        return authenticationMicroserviceAdapter.authenticate(request);
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws ResponseStatusException if a user with this netid already exists
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequestModel request) {
        logger.info("Received register request for user: " + request.getNetId());
        return authenticationMicroserviceAdapter.register(request);
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
