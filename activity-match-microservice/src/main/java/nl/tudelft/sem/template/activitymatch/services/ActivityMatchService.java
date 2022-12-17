package nl.tudelft.sem.template.activitymatch.services;

import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.domain.TypesOfActivities;
import nl.tudelft.sem.template.activitymatch.models.MatchCreationRequestModel;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import org.springframework.stereotype.Service;


//import java.time.LocalDateTime;
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

}
