package nl.tudelft.sem.template.activitymatch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activitymatch.domain.ActivityJoinQueueEntry;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityJoinQueueRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivityMatchCreationService {

    private final transient ActivityMatchRepository activityMatchRepository;
    private final transient ActivityJoinQueueRepository activityJoinQueueRepository;

    /**
     * Instantiates a new MatchCreationService.
     *
     * @param activityMatchRepository activityMatchRepository
     */
    public ActivityMatchCreationService(ActivityMatchRepository activityMatchRepository,
                                        ActivityJoinQueueRepository activityJoinQueueRepository) {
        this.activityMatchRepository = activityMatchRepository;
        this.activityJoinQueueRepository = activityJoinQueueRepository;
    }

    /**
     * Creates a new TrainingOffer and adds it to database.
     *
     * @param request TrainingOffer model sent by user
     */
    public void createActivityMatch(MatchCreationRequestModel request) {
        String ownerId = request.getOwnerId();
        String activityId = request.getActivityId();
        String userId = request.getUserId();
        TypesOfActivities type = request.getType();
        ActivityMatch activityMatch = new ActivityMatch(ownerId, activityId, userId, type);
        activityMatchRepository.save(activityMatch);
        System.out.printf("Activity %s has been added to the database\n", activityMatch);
    }

    /**
     * Retrieves the pending offers for a given activity ID.
     *
     * @param request the request wrapped in a PendingOffersRequestModel.
     * @return the response wrapped in a PendingOffersResponseModel.
     * @throws ResponseStatusException when the given activity ID does not exist.
     */
    public PendingOffersResponseModel getPendingOffers(PendingOffersRequestModel request)
            throws ResponseStatusException {
        int activityId = request.getActivityId();
        Optional<ActivityMatch> activityMatch = activityMatchRepository.findByActivityId(activityId);

        ActivityMatchService.checkActivityMatch(activityMatch, request.getActivityId());

        int activityMatchId = activityMatch.get().getId();
        Optional<List<ActivityJoinQueueEntry>> activityJoinQueue = activityJoinQueueRepository
                .findByActivityMatchId(activityMatchId);

        if (activityJoinQueue.isEmpty()) {
            return new PendingOffersResponseModel(new ArrayList<>());
        }

        return new PendingOffersResponseModel(activityJoinQueue.get().stream()
                .map(joinQueueEntry ->
                        new PendingOffersResponseModel.JoinRequest(joinQueueEntry.getEnrolledUserNetId()))
                .collect(Collectors.toList()));
    }
}
