package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.domain.ActivityMatch;
import nl.tudelft.sem.template.example.domain.TypesOfActivities;
import nl.tudelft.sem.template.example.models.MatchCreationRequestModel;
import nl.tudelft.sem.template.example.repositories.ActivityMatchRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
@Service
public class ActivityMatchService {
    private final transient ActivityMatchRepository activityMatchRepository;

    /**
     * Instantiates a new ActivityMatchService.
     *
     * @param activityMatchRepository activityMatchRepository
     */
    public ActivityMatchService(ActivityMatchRepository activityMatchRepository) {
        this.activityMatchRepository = activityMatchRepository;
    }

    /**
     * Creates a new TrainingOffer and adds it to database.
     *
     * @param request TrainingOffer model sent by user
     * @throws Exception exception
     */
    public void createActivityMatch(MatchCreationRequestModel request) throws Exception {
        try {

            String ownerId = request.getOwnerId();
            String activityId = request.getActivityId();
            String userId = request.getUserId();
            TypesOfActivities type = request.getType();
            ActivityMatch activityMatch = new ActivityMatch(ownerId, activityId, userId, type);
            activityMatchRepository.save(activityMatch);
            System.out.println("Activity " + activityMatch.toString() + " has been added to the database");
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while creating ActivityMatch. " + e.getMessage());
        }
    }

    /**
     * 
     */
    public void getPendingOffers(PendingOffersRequestModel request) throws Exception {
        try {
            int activityId = request.getActivityId();
            Optional<ActivityMatch> activityMatch = activityMatchRepository.findById(activityId);

            if (pendingOffers.isEmpty()) {
                throw new Exception("No pending offers for user " + userId + " have been found");
            }

            // TODO: return pending offers
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while retrieving pending offers. " + e.getMessage());
        }
    }
}
