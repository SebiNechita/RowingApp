package nl.tudelft.sem.template.activitymatch.controllers;

import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActivityMatchController {
    protected final transient ActivityMatchService activityMatchService;
    static final Logger logger = LoggerFactory.getLogger(ActivityMatchController.class.getName());

    /**
     * Instantiates a new ActivityMatchController.
     *
     * @param activityMatchService activityMatchService
     */
    public ActivityMatchController(ActivityMatchService activityMatchService) {
        this.activityMatchService = activityMatchService;
    }
}
