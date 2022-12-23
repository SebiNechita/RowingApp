package nl.tudelft.sem.template.common.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ActivityMatchMicroserviceAdapter {
    public final transient String activityMatchMicroserviceAddress;


    /**
     * Instantiates a new AuthenticationMicroserviceAdapter.
     *
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
    public ResponseEntity<Void> createActivityMatch(MatchCreationRequestModel request, String authToken)
            throws ResponseStatusException {
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
                                                                       String authToken)
            throws ResponseStatusException {
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
    public ResponseEntity<String> setParticipant(SetParticipantRequestModel request, String authToken)
            throws ResponseStatusException {
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
    public ResponseEntity<String> addUserToJoinQueue(AddUserToJoinQueueRequestModel request, String authToken)
            throws ResponseStatusException {
        return HttpUtils.sendAuthorizedHttpRequest(addUserToJoinQueueEndpointUrl(), HttpMethod.POST, authToken, request,
                String.class);
    }
}
