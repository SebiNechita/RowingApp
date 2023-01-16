package nl.tudelft.sem.template.common.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activity.AvailableCompetitionsModel;
import nl.tudelft.sem.template.common.models.activity.AvailableTrainingsModel;
import nl.tudelft.sem.template.common.models.activity.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ActivityOfferMicroserviceAdapter {

    public final transient String activityOfferMicroserviceAddress;

    /**
     * Instantiates a new UserMicroserviceAdapter.
     *
     */
    public ActivityOfferMicroserviceAdapter() {
        this.activityOfferMicroserviceAddress = MicroServiceAddresses.activityOfferMicroservice;
    }

    /**
     * Instantiates a new UserMicroserviceAdapter with an injected microservice address.
     *
     * @param activityOfferMicroserviceAddress The address of the microservice.
     */
    public ActivityOfferMicroserviceAdapter(String activityOfferMicroserviceAddress) {
        this.activityOfferMicroserviceAddress = activityOfferMicroserviceAddress;
    }

    private String participantIsEligibleEndpointUrl() {
        return activityOfferMicroserviceAddress + "/competition/participant-is-eligible";
    }

    /**
     * Provides an url for fetching available competitions.
     *
     * @return Url
     */
    private String fetchAvailableCompetitionsUrl() {
        return activityOfferMicroserviceAddress + "/get/competitions/filtered";
    }

    /**
     * Endpoint for checking if a participant is eligible to join a given activity.
     *
     * @param request wrapped in a ParticipantIsEligibleRequestModel.
     * @return boolean indicating eligibility.
     * @throws ResponseStatusException if not successful.
     */
    public ResponseEntity<Boolean> participantIsEligible(ParticipantIsEligibleRequestModel request, String authToken)
            throws ResponseStatusException {
        return HttpUtils.sendAuthorizedHttpRequest(participantIsEligibleEndpointUrl(), HttpMethod.POST, authToken,
                request, Boolean.class);
    }


    /**
     * Provides an url for creating a new competition.
     *
     * @return Url
     */
    private String createCompetitionUrl() {
        return activityOfferMicroserviceAddress + "/create/competition";
    }

    /**
     * Provides an url for getting a list of filtered activitys based on user's availabilty.
     *
     * @return Url
     */
    private String getFilteredTrainingsForUserUrl(NetId netId) {
        return activityOfferMicroserviceAddress + "/get/trainings/" + netId;
    }

    /**
     * Endpoint for creating a new competition.
     *
     * @param request   request wrapped in a CompetitionCreationRequestModel
     * @param authToken authentication token
     * @return status of the message
     */
    public ResponseEntity<String> createCompetition(CompetitionCreationRequestModel request, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(createCompetitionUrl(), HttpMethod.POST, authToken, request,
                String.class);
    }

    public ResponseEntity<AvailableTrainingsModel> getFilteredTrainings(NetId netId, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(getFilteredTrainingsForUserUrl(netId), HttpMethod.GET, authToken, netId,
                AvailableTrainingsModel.class);
    }

    /**
     * Endpoint for fetching available competitions for a certain user.
     *
     * @param netId     netId
     * @param authToken authToken
     * @return AvailableCompetitionsModel
     */
    public ResponseEntity<AvailableCompetitionsModel> fetchAvailableCompetitions(NetId netId, String authToken) {
        return HttpUtils.sendAuthorizedHttpRequest(fetchAvailableCompetitionsUrl(), HttpMethod.GET, authToken,
                netId, AvailableCompetitionsModel.class);
    }
}
