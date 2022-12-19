package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.common.models.activity_match.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activity_match.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activity_match.SetParticipantRequestModel;
import nl.tudelft.sem.template.example.services.ActivityMatchService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import nl.tudelft.sem.template.example.models.MatchCreationRequestModel;
import nl.tudelft.sem.template.example.services.ActivityMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ActivityMatchController {
    private final transient ActivityMatchService activityMatchService;
    private final transient Logger logger;

    /**
     * Instantiates a new ActivityMatchController.
     *
     * @param activityMatchService activityMatchService
     */
    @Autowired
    public ActivityMatchController(ActivityMatchService activityMatchService, Logger logger) {
        this.activityMatchService = activityMatchService;
        this.logger = logger;
    }

    /**
     * Endpoint for creating a new offer.
     *
     * @param request request
     * @return ok response if successful
     * @throws Exception if not successful
     */
    @PostMapping("/create/match")
    public ResponseEntity createActivityMatch(@RequestBody MatchCreationRequestModel request) throws Exception {
        try {
            activityMatchService.createActivityMatch(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/offers/pending")
    public ResponseEntity<PendingOffersResponseModel> getPendingOffers(@RequestBody PendingOffersRequestModel request)
            throws Exception {
        try {
            return ResponseEntity.ok(activityMatchService.getPendingOffers(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/set/participant")
    public ResponseEntity<String> setParticipant(@RequestBody SetParticipantRequestModel request)
            throws Exception {
        try {
            activityMatchService.setParticipant(request);
            return ResponseEntity.ok("Successfully set participant");
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
