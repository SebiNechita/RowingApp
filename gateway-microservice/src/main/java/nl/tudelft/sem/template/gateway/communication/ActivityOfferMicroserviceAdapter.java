package nl.tudelft.sem.template.gateway.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activity.AvailableCompetitionsModel;
import nl.tudelft.sem.template.common.models.activity.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ActivityOfferMicroserviceAdapter {

    public final transient String activityOfferMicroserviceAddress;

    /**
     * Instantiates a new ActivityOfferMicroserviceAdapter.
     */
    public ActivityOfferMicroserviceAdapter() {
        this.activityOfferMicroserviceAddress = MicroServiceAddresses.activityOfferMicroservice;
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
     * Provides an url for fetching available competitions.
     *
     * @return Url
     */
    private String fetchAvailableCompetitionsUrl() {
        return activityOfferMicroserviceAddress + "/get/competitions/filtered";
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
