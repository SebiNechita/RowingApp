package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.models.MatchCreationRequestModel;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ActivityMatchController {
    private final transient ActivityMatchService activityMatchService;

    /**
     * Instantiates a new ActivityMatchController.
     *
     * @param activityMatchService activityMatchService
     */
    @Autowired
    public ActivityMatchController(ActivityMatchService activityMatchService) {
        this.activityMatchService = activityMatchService;
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
}
