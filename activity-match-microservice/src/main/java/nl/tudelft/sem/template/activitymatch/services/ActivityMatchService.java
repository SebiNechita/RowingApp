package nl.tudelft.sem.template.activitymatch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activitymatch.domain.ActivityJoinQueueEntry;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.domain.ActivityParticipant;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityJoinQueueRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityParticipantRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.springframework.stereotype.Service;

@Service
public class ActivityMatchService {
    private final transient ActivityMatchRepository activityMatchRepository;
    private final transient ActivityJoinQueueRepository activityJoinQueueRepository;
    private final transient ActivityParticipantRepository activityParticipantRepository;

    /**
     * Instantiates a new ActivityMatchService.
     *
     * @param activityMatchRepository activityMatchRepository
     * @param activityJoinQueueRepository activityJoinQueueRepository
     * @param activityParticipantRepository activityParticipantRepository
     */
    public ActivityMatchService(ActivityMatchRepository activityMatchRepository,
                                ActivityJoinQueueRepository activityJoinQueueRepository,
                                ActivityParticipantRepository activityParticipantRepository) {
        this.activityMatchRepository = activityMatchRepository;
        this.activityJoinQueueRepository = activityJoinQueueRepository;
        this.activityParticipantRepository = activityParticipantRepository;
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
     * Retrieves the pending offers for a given activity ID.
     *
     * @param request the request wrapped in a PendingOffersRequestModel.
     * @return the response wrapped in a PendingOffersResponseModel.
     * @throws Exception when the given activity ID does not exist.
     */
    public PendingOffersResponseModel getPendingOffers(PendingOffersRequestModel request) throws Exception {
        try {
            int activityId = request.getActivityId();
            Optional<ActivityMatch> activityMatch = activityMatchRepository.findById(activityId);

            if (activityMatch.isEmpty()) {
                throw new Exception("Activity " + request.getActivityId() + " was not found");
            }

            int activityMatchId = activityMatch.get().getId();
            Optional<List<ActivityJoinQueueEntry>> activityJoinQueue = activityJoinQueueRepository
                    .findByActivityMatchId(activityMatchId);

            if (activityJoinQueue.isEmpty()) {
                return new PendingOffersResponseModel(new ArrayList<>());
            }

            return new PendingOffersResponseModel(activityJoinQueue.get().stream()
                    .map(joinQueueEntry ->
                            new PendingOffersResponseModel.JoinRequest(joinQueueEntry.getEnrolledUserId()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while retrieving pending offers. " + e.getMessage());
        }
    }

    /**
     * Sets the participant of a given activity by its activity ID.
     *
     * @param request the request wrapped in a SetParticipantRequestModel.
     * @throw Exception when the given activity ID does not exist.
     */
    public void setParticipant(SetParticipantRequestModel request) throws Exception {
        try {
            int activityId = request.getActivityId();
            Optional<ActivityMatch> activityMatch = activityMatchRepository.findById(activityId);

            if (activityMatch.isEmpty()) {
                throw new Exception("Activity " + request.getActivityId() + " was not found");
            }

            int activityMatchId = activityMatch.get().getId();
            String participantNetId = request.getParticipantNetId();

            activityParticipantRepository.save(new ActivityParticipant(activityMatchId, participantNetId));
        } catch (Exception e) {
            System.out.println("Exception in the service");
            throw new Exception("Error while setting participant. " + e.getMessage());
        }
    }
}
