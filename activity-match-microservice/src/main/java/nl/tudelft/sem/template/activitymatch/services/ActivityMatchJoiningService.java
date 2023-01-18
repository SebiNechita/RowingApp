package nl.tudelft.sem.template.activitymatch.services;

import nl.tudelft.sem.template.activitymatch.domain.ActivityJoinQueueEntry;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.domain.ActivityParticipant;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityJoinQueueRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityParticipantRepository;
import nl.tudelft.sem.template.common.communication.ActivityOfferMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityMatchJoiningService {
    private final transient ActivityJoinQueueRepository activityJoinQueueRepository;
    private final transient ActivityMatchRepository activityMatchRepository;
    private final transient ActivityParticipantRepository activityParticipantRepository;
    private final transient ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter;

    /**
     * Instantiates a new ActivityMatchJoiningService.
     *
     * @param activityMatchRepository activityMatchRepository
     * @param activityJoinQueueRepository activityJoinQueueRepository
     * @param activityParticipantRepository activityParticipantRepository
     * @param activityOfferMicroserviceAdapter activityOfferMicroserviceAdapter
     */
    public ActivityMatchJoiningService(ActivityMatchRepository activityMatchRepository,
                                ActivityJoinQueueRepository activityJoinQueueRepository,
                                ActivityParticipantRepository activityParticipantRepository,
                                ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter) {
        this.activityMatchRepository = activityMatchRepository;
        this.activityJoinQueueRepository = activityJoinQueueRepository;
        this.activityParticipantRepository = activityParticipantRepository;
        this.activityOfferMicroserviceAdapter = activityOfferMicroserviceAdapter;
    }

    /**
     * Checks if participant is eligible.
     *
     * @param activityMatch activityMatch.
     * @param participantId participantId.
     * @throws ResponseStatusException when the given activity ID does not exist.
     */
    public void invalidParticipant(Optional<ActivityMatch> activityMatch, String participantId)
            throws ResponseStatusException {

        int activityMatchId = activityMatch.get().getId();

        Optional<List<ActivityJoinQueueEntry>> activityJoinQueue = activityJoinQueueRepository
                .findByActivityMatchId(activityMatchId);

        if (activityJoinQueue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users are enrolled in this activity");
        }

        if (activityJoinQueue.get().stream()
                .noneMatch(entry -> entry.getEnrolledUserNetId().equals(participantId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The selected user was not found in the join queue of this activity");
        }
    }

    /**
     * Sets the participant of a given activity by its activity ID.
     *
     * @param request the request wrapped in a SetParticipantRequestModel.
     * @throw ResponseStatusException when the given activity ID does not exist, or the caller of the endpoint is not
     *                                the owner of the activity.
     */
    public void setParticipant(SetParticipantRequestModel request)
            throws ResponseStatusException {
        int activityId = request.getActivityId();
        String participantId = request.getParticipantNetId();
        Optional<ActivityMatch> activityMatch = activityMatchRepository.findByActivityId(activityId);

        ActivityMatchService.checkActivityMatch(activityMatch, activityId);

        invalidParticipant(activityMatch, participantId);

        activityParticipantRepository.save(new ActivityParticipant(activityMatch.get().getId(), participantId));
    }

    /**
     * Adds a user to the join queue of an activity.
     *
     * @param request the request wrapped in an AddUserToJoinQueueRequestModel
     * @throws ResponseStatusException when the activity does not exist.
     */
    public void addUserToJoinQueue(AddUserToJoinQueueRequestModel request, String userNetId, String authToken)
            throws ResponseStatusException {
        int activityId = request.getActivityId();
        Optional<ActivityMatch> activityMatch = activityMatchRepository.findByActivityId(activityId);

        ActivityMatchService.checkActivityMatch(activityMatch, activityId);

        Boolean eligible = activityOfferMicroserviceAdapter.participantIsEligible(new ParticipantIsEligibleRequestModel(
                activityId, userNetId
        ), authToken).getBody();

        if (!eligible) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not eligible to join this activity");
        }

        int activityMatchId = activityMatch.get().getId();
        activityJoinQueueRepository.save(new ActivityJoinQueueEntry(activityMatchId, userNetId));
    }
}
