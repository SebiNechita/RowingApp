package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.services.ActivityMatchJoiningService;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActivityMatchController {
    protected final transient ActivityMatchService activityMatchService;
    protected final transient ActivityMatchCreationService activityMatchCreationService;
    protected final transient ActivityMatchJoiningService activityMatchJoiningService;
    static final Logger logger = LoggerFactory.getLogger(ActivityMatchController.class.getName());

    /**
     * Instantiates a new ActivityMatchController.
     *
     * @param activityMatchService activityMatchService
     */
    public ActivityMatchController(ActivityMatchService activityMatchService,
                                   ActivityMatchCreationService activityMatchCreationService,
                                   ActivityMatchJoiningService activityMatchJoiningService) {
        this.activityMatchService = activityMatchService;
        this.activityMatchCreationService = activityMatchCreationService;
        this.activityMatchJoiningService = activityMatchJoiningService;
    }
}
