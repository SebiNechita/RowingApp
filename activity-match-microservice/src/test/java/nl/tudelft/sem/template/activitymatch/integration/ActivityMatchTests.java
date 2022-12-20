package nl.tudelft.sem.template.activitymatch.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.template.activitymatch.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import nl.tudelft.sem.template.activitymatch.domain.TypesOfActivities;
import nl.tudelft.sem.template.activitymatch.integration.utils.JsonUtil;
import nl.tudelft.sem.template.activitymatch.models.MatchCreationRequestModel;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityMatchRepository;
import nl.tudelft.sem.template.activitymatch.services.ActivityMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockTokenVerifier"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ActivityMatchTests {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;
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
    public void createTraining_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/create/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.serialize(requestModel))
                        .header("Authorization", "Bearer MockedToken"));

        // Assert
        resultActions.andExpect(status().isOk());

        ActivityMatch activityOffer = activityMatchRepository.findById(1).orElseThrow();

        assertThat(activityOffer.getUserId()).isEqualTo(userId);
        assertThat(activityOffer.getActivityId()).isEqualTo(activityId);
        assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityOffer.getType()).isEqualTo(type);
    }
}