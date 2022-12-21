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
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (activityMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Activity %d was not found", request.getActivityId()));
        }

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

    /**
     * Sets the participant of a given activity by its activity ID.
     *
     * @param request the request wrapped in a SetParticipantRequestModel.
     * @throw ResponseStatusException when the given activity ID does not exist, or the caller of the endpoint is not
     *                                the owner of the activity.
     */
    public void setParticipant(SetParticipantRequestModel request, String ownerNetId) throws ResponseStatusException {
        int activityId = request.getActivityId();
        Optional<ActivityMatch> activityMatch = activityMatchRepository.findByActivityId(activityId);

        if (activityMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Activity %d was not found", request.getActivityId()));
        }

        if (!activityMatch.get().getOwnerId().equals(ownerNetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not the owner of this activity");
        }

        int activityMatchId = activityMatch.get().getId();

        Optional<List<ActivityJoinQueueEntry>> activityJoinQueue = activityJoinQueueRepository
                .findByActivityMatchId(activityMatchId);

        if (activityJoinQueue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users are enrolled in this activity");
        }

        if (activityJoinQueue.get().stream()
                .noneMatch(entry -> entry.getEnrolledUserNetId().equals(request.getParticipantNetId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The selected user was not found in the join queue of this activity");
        }

        activityParticipantRepository.save(new ActivityParticipant(activityMatchId, request.getParticipantNetId()));
    }

    /**
     * Adds a user to the join queue of an activity.
     *
     * @param request the request wrapped in an AddUserToJoinQueueRequestModel
     * @throws ResponseStatusException when the activity does not exist.
     */
    public void addUserToJoinQueue(AddUserToJoinQueueRequestModel request, String userNetId)
            throws ResponseStatusException {
        int activityId = request.getActivityId();
        Optional<ActivityMatch> activityMatch = activityMatchRepository.findByActivityId(activityId);

        if (activityMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Activity %d was not found", activityId));
        }

        int activityMatchId = activityMatch.get().getId();
        activityJoinQueueRepository.save(new ActivityJoinQueueEntry(activityMatchId, userNetId));
    }
}
