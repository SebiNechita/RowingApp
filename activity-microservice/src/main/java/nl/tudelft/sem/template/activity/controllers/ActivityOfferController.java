package nl.tudelft.sem.template.activity.controllers;

import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActivityOfferController {
    protected final transient ActivityOfferService activityOfferService;
    protected final transient UserMicroserviceAdapter userMicroserviceAdapter;
    static final Logger logger = LoggerFactory.getLogger(ActivityOfferController.class.getName());

    /**
     * Constructor for an ActivityOfferController.
     *
     * @param activityOfferService    activityOfferService
     * @param userMicroserviceAdapter userMicroserviceAdapter
     */
    public ActivityOfferController(ActivityOfferService activityOfferService,
                                   UserMicroserviceAdapter userMicroserviceAdapter) {
        this.activityOfferService = activityOfferService;
        this.userMicroserviceAdapter = userMicroserviceAdapter;
    }
}
