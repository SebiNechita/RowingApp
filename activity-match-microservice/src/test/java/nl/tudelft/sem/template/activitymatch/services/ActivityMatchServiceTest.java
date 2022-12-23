package nl.tudelft.sem.template.activitymatch.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.activitymatch.domain.ActivityJoinQueueEntry;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.domain.ActivityParticipant;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityJoinQueueRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityParticipantRepository;
import nl.tudelft.sem.template.common.communication.ActivityOfferMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activitymatch.AddUserToJoinQueueRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersRequestModel;
import nl.tudelft.sem.template.common.models.activitymatch.PendingOffersResponseModel;
import nl.tudelft.sem.template.common.models.activitymatch.SetParticipantRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityMatchServiceTest {

    private ActivityMatchService activityMatchService;
    @Mock
    private ActivityMatchRepository activityMatchRepository;
    @Mock
    private ActivityJoinQueueRepository activityJoinQueueRepository;
    @Mock
    private ActivityParticipantRepository activityParticipantRepository;
    @Mock
    private ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter;

    @BeforeEach
    void setup() {
        this.activityMatchService = new ActivityMatchService(activityMatchRepository,
                activityJoinQueueRepository, activityParticipantRepository, activityOfferMicroserviceAdapter);
    }

    @Test
    public void createActivity_withValidData_worksCorrectly() {
        String userId = "1234";
        String activityId = "5678";
        String ownerId = "papiez";
        TypesOfActivities type = TypesOfActivities.TRAINING;
        MatchCreationRequestModel req = new MatchCreationRequestModel(ownerId, activityId, userId, type);

        activityMatchService.createActivityMatch(req);

        when(activityMatchRepository.findById(1)).thenReturn(Optional.of(new ActivityMatch(
                req.getOwnerId(),
                req.getActivityId(),
                req.getUserId(),
                req.getType()
        )));

        ActivityMatch activityMatch = activityMatchRepository.findById(1).orElseThrow();

        assertThat(activityMatch.getUserId()).isEqualTo(userId);
        assertThat(activityMatch.getActivityId()).isEqualTo(activityId);
        assertThat(activityMatch.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityMatch.getType()).isEqualTo(type);
    }

    @Test
    public void getPendingOffersOnNonExistentActivityThrows404() {
        int activityId = 123;
        PendingOffersRequestModel req = new PendingOffersRequestModel(activityId);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.empty());

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.getPendingOffers(req));

        assertThat(exc.getStatus().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getPendingOffersOnEmptyJoinQueueReturnsEmptyList() {
        int activityId = 123;
        PendingOffersRequestModel req = new PendingOffersRequestModel(activityId);

        ActivityMatch activityMatch = new ActivityMatch("69", "123", "420",
                TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));

        PendingOffersResponseModel res = activityMatchService.getPendingOffers(req);

        assertThat(res.getJoinRequests().isEmpty());
    }

    @Test
    public void getPendingOffersOnEmptyJoinQueueHappyFlow() {
        int activityId = 123;
        PendingOffersRequestModel req = new PendingOffersRequestModel(activityId);

        ActivityMatch activityMatch = new ActivityMatch("69", Integer.toString(activityId), "420",
                TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityJoinQueueRepository.findByActivityMatchId(activityMatch.getId()))
                .thenReturn(Optional.of(List.of(
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Rick Astley"),
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Shrek")
                )));

        PendingOffersResponseModel res = activityMatchService.getPendingOffers(req);

        assertThat(res.getJoinRequests().containsAll(List.of("Rick Astley", "Shrek")));
    }

    @Test
    public void setParticipantOnNonExistentActivityThrows404() {
        int activityId = 123;
        String userId = "Shrek";
        SetParticipantRequestModel req = new SetParticipantRequestModel(activityId, userId);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.empty());

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.setParticipant(req, userId));

        assertThat(exc.getStatus().equals(HttpStatus.NOT_FOUND));

        verify(activityParticipantRepository, never()).save(any());
    }

    @Test
    public void setParticipantOnNotOwnerOfActivityThrows401() {
        int activityId = 123;
        String userId = "Shrek";
        SetParticipantRequestModel req = new SetParticipantRequestModel(activityId, userId);

        ActivityMatch activityMatch = new ActivityMatch("Donkey Kong", Integer.toString(activityId),
                "Rick Astley", TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.setParticipant(req, userId));

        assertThat(exc.getStatus().equals(HttpStatus.UNAUTHORIZED));

        verify(activityParticipantRepository, never()).save(any());
    }

    @Test
    public void setParticipantOnEmptyJoinQueueThrow404() {
        int activityId = 123;
        String userId = "Shrek";
        SetParticipantRequestModel req = new SetParticipantRequestModel(activityId, userId);

        ActivityMatch activityMatch = new ActivityMatch(userId, Integer.toString(activityId),
                userId, TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityJoinQueueRepository.findByActivityMatchId(activityMatch.getId()))
                .thenReturn(Optional.empty());

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.setParticipant(req, userId));

        assertThat(exc.getStatus().equals(HttpStatus.NOT_FOUND));

        verify(activityParticipantRepository, never()).save(any());
    }

    @Test
    public void setParticipantOnUserNotInJoinQueueThrow404() {
        int activityId = 123;
        String userId = "Shrek";
        SetParticipantRequestModel req = new SetParticipantRequestModel(activityId, userId);

        ActivityMatch activityMatch = new ActivityMatch(userId, Integer.toString(activityId),
                userId, TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityJoinQueueRepository.findByActivityMatchId(activityMatch.getId()))
                .thenReturn(Optional.of(List.of(
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Mario"),
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Luigi")
                )));

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.setParticipant(req, userId));

        assertThat(exc.getStatus().equals(HttpStatus.NOT_FOUND));

        verify(activityParticipantRepository, never()).save(any());
    }

    @Test
    public void setParticipantHappyFlow() {
        int activityId = 123;
        String userId = "Shrek";
        SetParticipantRequestModel req = new SetParticipantRequestModel(activityId, userId);

        ActivityMatch activityMatch = new ActivityMatch(userId, Integer.toString(activityId),
                userId, TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityJoinQueueRepository.findByActivityMatchId(activityMatch.getId()))
                .thenReturn(Optional.of(List.of(
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Shrek"),
                        new ActivityJoinQueueEntry(activityMatch.getId(), "Luigi")
                )));

        activityMatchService.setParticipant(req, userId);

        verify(activityParticipantRepository, times(1))
                .save(new ActivityParticipant(activityMatch.getId(), userId));
    }

    @Test
    public void addUserToJoinQueueOnNonExistentActivityThrow404() {
        int activityId = 123;
        String userId = "Shrek";
        AddUserToJoinQueueRequestModel req = new AddUserToJoinQueueRequestModel(activityId);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.empty());

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.addUserToJoinQueue(req, userId, "mock-token"));

        assertThat(exc.getStatus().equals(HttpStatus.NOT_FOUND));

        verify(activityJoinQueueRepository, never()).save(any());
    }

    @Test
    public void addUserToJoinQueueIneligible() {
        int activityId = 123;
        String userId = "Shrek";
        AddUserToJoinQueueRequestModel req = new AddUserToJoinQueueRequestModel(activityId);

        ActivityMatch activityMatch = new ActivityMatch(userId, Integer.toString(activityId),
                userId, TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityOfferMicroserviceAdapter.participantIsEligible(any(), any())).thenReturn(ResponseEntity.ok(false));

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> activityMatchService.addUserToJoinQueue(req, userId, "mock-token"));

        assertThat(exc.getStatus().equals(HttpStatus.FORBIDDEN));

        verify(activityJoinQueueRepository, never()).save(any());
    }

    @Test
    public void addUserToJoinQueueHappyFlow() {
        int activityId = 123;
        String userId = "Shrek";
        AddUserToJoinQueueRequestModel req = new AddUserToJoinQueueRequestModel(activityId);

        ActivityMatch activityMatch = new ActivityMatch(userId, Integer.toString(activityId),
                userId, TypesOfActivities.TRAINING);

        when(activityMatchRepository.findByActivityId(activityId)).thenReturn(Optional.of(activityMatch));
        when(activityOfferMicroserviceAdapter.participantIsEligible(any(), any())).thenReturn(ResponseEntity.ok(true));

        activityMatchService.addUserToJoinQueue(req, userId, "mock-token");

        verify(activityJoinQueueRepository, times(1))
                .save(new ActivityJoinQueueEntry(activityMatch.getId(), userId));
    }
}
