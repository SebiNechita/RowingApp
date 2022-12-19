package nl.tudelft.sem.template.activitymatch.services;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activitymatch.MatchCreationRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityMatchServiceTest {

    @Autowired
    private transient ActivityMatchService activityService;
    @Autowired
    private transient ActivityMatchRepository activityMatchRepository;

    private MatchCreationRequestModel requestModel;

    private String userId;

    private String activityId;

    private String ownerId;

    private TypesOfActivities type;

    @BeforeEach
    void setup() {
        // Arrange
        this.userId = "1234";
        this.activityId = "5678";
        this.ownerId = "papiez";
        this.type = TypesOfActivities.TRAINING;
        this.requestModel = new MatchCreationRequestModel(ownerId, activityId, userId, type);

    }

    @Test
    public void createActivity_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createActivityMatch(requestModel);

        // Assert
        ActivityMatch activityOffer = activityMatchRepository.findById(1).orElseThrow();

        assertThat(activityOffer.getUserId()).isEqualTo(userId);
        assertThat(activityOffer.getActivityId()).isEqualTo(activityId);
        assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityOffer.getType()).isEqualTo(type);
    }
}
