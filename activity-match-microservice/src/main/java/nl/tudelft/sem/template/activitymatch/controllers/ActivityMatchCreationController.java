package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.services.ActivityMatchCreationService;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ActivityMatchCreationController {
    protected final transient ActivityMatchCreationService activityMatchCreationService;
    static final Logger logger = LoggerFactory.getLogger(ActivityMatchCreationController.class.getName());

    /**
     * Instantiates a new ActivityMatchCreationController.
     *
     * @param activityMatchCreationService matchCreationService
     */
    @Autowired
    public ActivityMatchCreationController(ActivityMatchCreationService activityMatchCreationService) {
        this.activityMatchCreationService = activityMatchCreationService;
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
            activityMatchCreationService.createActivityMatch(request);
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
            return ResponseEntity.ok(activityMatchCreationService.getPendingOffers(request));
        } catch (ResponseStatusException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
