package nl.tudelft.sem.template.activity.services;

import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;

public class CompetitionOfferService extends ActivityOfferService{

    public CompetitionOfferService(ActivityOfferRepository activityOfferRepository,
                                   DataValidation dataValidation) {
        super(activityOfferRepository, dataValidation);
    }

}
