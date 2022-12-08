package nl.tudelft.sem.template.example.services;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.example.domain.TrainingOffer;
import nl.tudelft.sem.template.example.domain.TypesOfActivities;
import nl.tudelft.sem.template.example.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.example.repositories.ActivityOfferRepository;
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

            TrainingOffer training = new TrainingOffer(position, isActive, startTime, endTime,
                    ownerId, boatCertificate, type);
            activityOfferRepository.save(training);
        } catch (Exception e) {
            throw new Exception("Error while creating ActivityOffer " + e.getMessage());
        }

    }
}
