package nl.tudelft.sem.template.common.communication;

import nl.tudelft.sem.template.common.http.HttpUtils;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.user.GetUserDetailsModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ActivityOfferMicroserviceAdapter {

    public final transient String activityOfferMicroserviceAddress;

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
}
