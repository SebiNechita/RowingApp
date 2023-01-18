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
import nl.tudelft.sem.template.common.communication.ActivityOfferMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
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
    private final transient ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter;

    /**
     * Instantiates a new ActivityMatchService.
     *
     * @param activityMatchRepository activityMatchRepository
     * @param activityJoinQueueRepository activityJoinQueueRepository
     * @param activityParticipantRepository activityParticipantRepository
     * @param activityOfferMicroserviceAdapter activityOfferMicroserviceAdapter
     */
    public ActivityMatchService(ActivityMatchRepository activityMatchRepository,
                                ActivityJoinQueueRepository activityJoinQueueRepository,
                                ActivityParticipantRepository activityParticipantRepository,
                                ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter) {
        this.activityMatchRepository = activityMatchRepository;
        this.activityJoinQueueRepository = activityJoinQueueRepository;
        this.activityParticipantRepository = activityParticipantRepository;
        this.activityOfferMicroserviceAdapter = activityOfferMicroserviceAdapter;
    }

    /**
     * Checks activity match if activity is empty.
     *
     * @param activityMatch activityMatch.
     * @param id activityId.
     * @throws ResponseStatusException when the given activity ID does not exist.
     */
    public static void checkActivityMatch(Optional<ActivityMatch> activityMatch, int id)
            throws ResponseStatusException {
        if (activityMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Activity %d was not found", id));
        }
    }
}
