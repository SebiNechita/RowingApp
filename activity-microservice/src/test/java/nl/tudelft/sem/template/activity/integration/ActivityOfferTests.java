package nl.tudelft.sem.template.activity.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.template.activity.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.integration.utils.JsonUtil;
import nl.tudelft.sem.template.activity.models.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.activity.models.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
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
public class ActivityOfferTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    private TrainingCreationRequestModel requestModel;
    private CompetitionCreationRequestModel competitionRequestModel;
    private TypesOfPositions position;

    private ManyTrainingsCreationRequestModel manyTrainingsRequestModel;

    private Map<TypesOfPositions, Integer> positions;
    private boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private TypesOfActivities type;
    private String name;
    private String description;
    private String organisation;
    private boolean isFemale;
    private boolean isPro;

    @BeforeEach
    void setup() {
        // Arrange
        this.position = TypesOfPositions.COX;
        this.isActive = false;
        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0, 0));
        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(12, 0, 0));
        this.ownerId = "JohnP2";
        this.boatCertificate = "4+";
        this.type = TypesOfActivities.TRAINING;
        this.name = "Team Blue Training";
        this.description = "Pumping the iron all day long";
        this.organisation = "partia przyjaciol piwa";
        this.isFemale = true;
        this.isPro = false;

        this.requestModel = new TrainingCreationRequestModel(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description);
        this.requestModel = new TrainingCreationRequestModel(position, isActive, startTime, endTime,
                ownerId, boatCertificate, type, name, description);
        this.positions = new HashMap<>() {{
                put(TypesOfPositions.COX, 2);
                put(TypesOfPositions.COACH, 1);
            }};

        this.manyTrainingsRequestModel = new ManyTrainingsCreationRequestModel(positions, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description);
        this.competitionRequestModel = new CompetitionCreationRequestModel(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro);

    }

    @Test
    public void createTraining_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        resultActions.andExpect(status().isOk());

        ActivityOffer activityOffer = activityOfferRepository.findById(1).orElseThrow();

        assertThat(activityOffer.getPosition()).isEqualTo(position);
        assertThat(activityOffer.isActive()).isEqualTo(isActive);
        assertThat(activityOffer.getStartTime()).isEqualTo(startTime);
        assertThat(activityOffer.getEndTime()).isEqualTo(endTime);
        assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityOffer.getBoatCertificate()).isEqualTo(boatCertificate);
        assertThat(activityOffer.getType()).isEqualTo(type);
        assertThat(activityOffer.getName()).isEqualTo(name);
        assertThat(activityOffer.getDescription()).isEqualTo(description);
    }

    @Test
    public void createManyTrainings_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/training/many")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(manyTrainingsRequestModel))
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        int id = 1;
        for (Map.Entry<TypesOfPositions, Integer> entry : positions.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                ActivityOffer activityOffer = activityOfferRepository.findById(id).orElseThrow();
                id++;

                assertThat(activityOffer.getPosition()).isEqualTo(entry.getKey());
                assertThat(activityOffer.isActive()).isEqualTo(isActive);
                assertThat(activityOffer.getStartTime()).isEqualTo(startTime);
                assertThat(activityOffer.getEndTime()).isEqualTo(endTime);
                assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
                assertThat(activityOffer.getBoatCertificate()).isEqualTo(boatCertificate);
                assertThat(activityOffer.getType()).isEqualTo(type);
                assertThat(activityOffer.getName()).isEqualTo(name);
                assertThat(activityOffer.getDescription()).isEqualTo(description);

            }
        }
    }

    @Test
    public void createCompetition_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/create/competition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.serialize(competitionRequestModel))
                        .header("Authorization", "Bearer MockedToken"));

        // Assert
        resultActions.andExpect(status().isOk());

        CompetitionOffer activityOffer = (CompetitionOffer) activityOfferRepository.findById(1).orElseThrow();

        assertThat(activityOffer.getPosition()).isEqualTo(position);
        assertThat(activityOffer.isActive()).isEqualTo(isActive);
        assertThat(activityOffer.getStartTime()).isEqualTo(startTime);
        assertThat(activityOffer.getEndTime()).isEqualTo(endTime);
        assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityOffer.getBoatCertificate()).isEqualTo(boatCertificate);
        assertThat(activityOffer.getType()).isEqualTo(type);
        assertThat(activityOffer.getName()).isEqualTo(name);
        assertThat(activityOffer.getDescription()).isEqualTo(description);
        assertThat(activityOffer.getOrganisation()).isEqualTo(organisation);
        assertThat(activityOffer.isFemale()).isEqualTo(isFemale);
        assertThat(activityOffer.isPro()).isEqualTo(isPro);
    }
}
