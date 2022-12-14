package nl.tudelft.sem.template.activity.services;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityOfferService {

    private final transient ActivityOfferRepository activityOfferRepository;

    /**
     * Instantiates a new ActivityOfferService.
     *
     * @param activityOfferRepository activityOfferRepository
     */
    public ActivityOfferService(ActivityOfferRepository activityOfferRepository) {
        this.activityOfferRepository = activityOfferRepository;
    }

    /**
     * Creates a new TrainingOffer and adds it to database.
     *
     * @param request TrainingOffer model sent by user
     * @throws Exception exception
     */
    public void createTrainingOffer(TrainingCreationRequestModel request) throws Exception {
        try {
            String position = request.getPosition();
            boolean isActive = request.isActive();
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = request.getEndTime();
            String ownerId = request.getOwnerId();
            String boatCertificate = request.getBoatCertificate();
            TypesOfActivities type = request.getType();
            String name = request.getName();
            String description = request.getDescription();
            // Do some checks if name and description are fine
            TrainingOffer training = new TrainingOffer(position, isActive, startTime, endTime,
                    ownerId, boatCertificate, type, name, description);
            activityOfferRepository.save(training);
            System.out.println("Training " + training.toString() + " has been added to the database");
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityOffer. " + e.getMessage());
        }

    }
}
