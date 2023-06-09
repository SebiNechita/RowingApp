package nl.tudelft.sem.template.activity.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.template.activity.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOfferBuilder;
import nl.tudelft.sem.template.activity.integration.utils.JsonUtil;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.activity.services.DataValidation;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import nl.tudelft.sem.template.common.models.activity.CompetitionCreationRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.NetId;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockUserMicroserviceAdapter", "mockTokenVerifier", "mockDataValidation"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CompetitionOfferTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;
    @Autowired
    private transient UserMicroserviceAdapter mockUserAdapter;
    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;
    @Autowired
    private transient DataValidation mockDataValidation;

    private CompetitionCreationRequestModel competitionRequestModel;
    private TypesOfPositions position;
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
    void setup() throws Exception {
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
        this.organisation = "Partia Przyjaciol Piwa";
        this.isFemale = true;
        this.isPro = false;

        this.competitionRequestModel = new CompetitionCreationRequestModel(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description, organisation, isFemale, isPro);

        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        when(mockDataValidation.validateOrganisation(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateCertificate(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateData(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateNameAndDescription(any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateTime(any(), any())).thenCallRealMethod();
    }

    @Test
    public void createCompetition_withValidData_worksCorrectly() throws Exception {
        // Arrange

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/competition")
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

    @Test
    public void fetchFilteredCompetitions_twoOffers_returnsOne() throws Exception {
        // Arrange
        CompetitionOfferBuilder builder = new CompetitionOfferBuilder();
        builder.setOrganisation(organisation)
                .setName(name)
                .setFemale(isFemale)
                .setDescription(description)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPosition(position)
                .setBoatCertificate(boatCertificate)
                .setOwnerId(ownerId)
                .setActive(true)
                .setPro(isPro);

        activityOfferRepository.save(builder.build());

        builder.setOrganisation("Too Good For You");

        activityOfferRepository.save(builder.build());


        UserDetailsModel user = new UserDetailsModel("NetId", "FEMALE", organisation, isPro,
                List.of(position),
                List.of(new Tuple(startTime, endTime)),
                List.of(boatCertificate));

        when(mockUserAdapter.getUserDetailsModel(any(), anyString())).thenReturn(ResponseEntity.ok(user));

        // Act
        ResultActions resultActions = mockMvc.perform(get("/get/competitions/filtered")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(new NetId("NetId")))
                .header("Authorization", "Bearer MockJWT"));

        // Assert
        resultActions.andExpect(status().isOk());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("\"id\":1"));
    }
}
