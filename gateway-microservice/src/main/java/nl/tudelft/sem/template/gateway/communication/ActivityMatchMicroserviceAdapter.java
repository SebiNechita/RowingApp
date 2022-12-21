package nl.tudelft.sem.template.gateway.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activitymatch.*;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationRequestModel;
import nl.tudelft.sem.template.common.models.authentication.AuthenticationResponseModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ActivityMatchMicroserviceAdapter {
    public final transient String activityMatchMicroserviceAddress;

    /**
     * Instantiates a new AuthenticationMicroserviceAdapter.
     */
    public ActivityMatchMicroserviceAdapter() {
        this.activityMatchMicroserviceAddress = MicroServiceAddresses.activityMatchMicroservice;
    }

    /**
     * Instantiates a new AuthenticationMicroserviceAdapter with an injected microservice address.
     *
     * @param activityMatchMicroserviceAddress The address of the microservice.
     */
    public ActivityMatchMicroserviceAdapter(String activityMatchMicroserviceAddress) {
        this.activityMatchMicroserviceAddress = activityMatchMicroserviceAddress;
    }

    private String createActivityMatchEndpointUrl() {
        return activityMatchMicroserviceAddress + "/create/match";
    }

    private String getPendingOffersEndpointUrl() {
        return activityMatchMicroserviceAddress + "/get/offers/pending";
    }

    private String setParticipantEndpointUrl() {
        return activityMatchMicroserviceAddress + "/set/participant";
    }

    private String addUserToJoinQueueEndpointUrl() {
        return activityMatchMicroserviceAddress + "/join-queue";
    }

    /**
     * Endpoint for creating a new offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws ResponseStatusException if not successful
     */
    public ResponseEntity<Void> createActivityMatch(MatchCreationRequestModel request, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(createActivityMatchEndpointUrl(), HttpMethod.POST, authToken,
                request, Void.class);
    }

    /**
     * Endpoint for retrieving a list of pending offers.
     *
     * @param request the request wrapped in a PendingOffersRequestModel
     * @return a response wrapped in a PendingOffersResponseModel
     * @throws ResponseStatusException if not successful
     */
    public ResponseEntity<PendingOffersResponseModel> getPendingOffers(PendingOffersRequestModel request,
                                                                       String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(getPendingOffersEndpointUrl(), HttpMethod.POST, authToken, request,
                PendingOffersResponseModel.class);
    }

    /**
     * Endpoint for setting the participant of an activity match.
     *
     * @param request the request wrapped in a SetParticipantRequestModel
     * @return a simple okay status message
     * @throws ResponseStatusException if not successful
     */
    public ResponseEntity<String> setParticipant(SetParticipantRequestModel request, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(setParticipantEndpointUrl(), HttpMethod.POST, authToken, request,
                String.class);
    }

    /**
     * Adds a user to the join queue of an activity.
     *
     * @param request the request wrapped in an AddUserToJoinQueueRequestModel
     * @return a simple okay status message
     * @throws ResponseStatusException if not successful
     */
    public ResponseEntity<String> addUserToJoinQueue(AddUserToJoinQueueRequestModel request, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(addUserToJoinQueueEndpointUrl(), HttpMethod.POST, authToken, request,
                String.class);
    }
}
