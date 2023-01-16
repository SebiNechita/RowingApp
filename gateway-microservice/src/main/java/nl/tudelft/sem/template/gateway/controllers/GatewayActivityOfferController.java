package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.common.communication.ActivityOfferMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.AvailableCompetitionsModel;
import nl.tudelft.sem.template.common.models.activity.AvailableTrainingsModel;
import nl.tudelft.sem.template.common.models.activity.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.common.models.user.NetId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayActivityOfferController extends GatewayController {

    private final transient ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter;
    private final transient UserMicroserviceAdapter userMicroserviceAdapter;

    /**
     * Instantiates a new Gateway Activity Offer Controller.
     *
     * @param activityOfferMicroserviceAdapter activityOfferMicroserviceAdapter
     * @param userMicroserviceAdapter          userMicroserviceAdapter
     */
    @Autowired
    public GatewayActivityOfferController(ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter,
                                          UserMicroserviceAdapter userMicroserviceAdapter) {
        this.activityOfferMicroserviceAdapter = activityOfferMicroserviceAdapter;
        this.userMicroserviceAdapter = userMicroserviceAdapter;
    }

    /**
     * Endpoint for creating a new competition.
     *
     * @param request   request wrapped in a CompetitionCreationRequestModel
     * @param authToken authentication token
     * @return status of the message
     */
    @PostMapping("/competition/create")
    public ResponseEntity<String> createCompetition(@RequestBody CompetitionCreationRequestModel request,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        logger.info(String.format("Received createCompetition request for the following offer: " + request.toString()));
        String userId = userMicroserviceAdapter.getUserId(authToken).getBody();

        request.setOwnerId(userId);
        return activityOfferMicroserviceAdapter.createCompetition(request, authToken);
    }

    /**
     * Endpoint for fetching available competitions for an authorized user.
     *
     * @param authToken authToken
     * @return AvailableCompetitionsModel
     */
    @GetMapping("/competitions/fetch-available")
    public ResponseEntity<AvailableCompetitionsModel> fetchAvailableCompetitions(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {

        String userId = userMicroserviceAdapter.getUserId(authToken).getBody();
        logger.info(String.format("Received fetchAvailableCompetitions request for the following user " + userId));

        return activityOfferMicroserviceAdapter.fetchAvailableCompetitions(new NetId(userId), authToken);
    }

    /**
     * Endpoint for getting a list of AvailableTrainingModel.
     *
     * @param netId     netId of the user
     * @param authToken authentication token
     * @return status of the message
     */
    @GetMapping("/get/trainings/{netId}")
    public ResponseEntity<AvailableTrainingsModel> getFilteredTrainingsForUser(
            @PathVariable("netId") NetId netId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        logger.info(String.format("Received getAvailableTrainings request for the following user: " + netId));
        return activityOfferMicroserviceAdapter.getFilteredTrainings(netId, authToken);
    }
}
