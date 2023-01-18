package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOfferBuilder;
import nl.tudelft.sem.template.activity.domain.TrainingOfferBuilder;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.CompetitionResponseModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.Tuple;
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
@ActiveProfiles({"test", "mockDataValidation"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompetitionOfferServiceTest {

    @Autowired
    private transient CompetitionOfferService activityService;
    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;
    @Autowired
    private transient DataValidation mockDataValidation;

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
    private String authToken;

    @BeforeEach
    void setup() throws Exception {
        // Arrange
        this.position = TypesOfPositions.COACH;
        this.isActive = true;
        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0, 0));
        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(12, 0, 0));
        this.ownerId = "papiez";
        this.boatCertificate = "C4";
        this.type = TypesOfActivities.TRAINING;
        this.name = "Team Blue Training";
        this.description = "Pumping the iron all day long";
        this.organisation = "Black Panthers";
        this.isFemale = true;
        this.isPro = false;

        when(mockDataValidation.validateOrganisation(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateCertificate(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateData(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateNameAndDescription(any(), any())).thenCallRealMethod();
    }

    @Test
    public void createTrainingActivity_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createCompetitionOffer(position, isActive, startTime, endTime, ownerId,
                boatCertificate, type, name, description, organisation, isFemale, isPro, authToken);

        // Assert
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
    public void fetchFilteredCompetitions_oneOffer_worksCorrectly() throws Exception {
        CompetitionOfferBuilder competitionBuilder = new CompetitionOfferBuilder();
        competitionBuilder.setActive(true)
                .setFemale(true)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPosition(TypesOfPositions.COX)
                .setBoatCertificate("C4")
                .setType(TypesOfActivities.COMPETITION)
                .setOwnerId("owner")
                .setDescription("description")
                .setName("name")
                .setOrganisation("Team Blue")
                .setPro(true);
        activityOfferRepository.save(competitionBuilder.build());

        List<CompetitionResponseModel> result = activityService.getFilteredCompetitionOffers(
                        "Team Blue",
                        true,
                        true,
                        List.of("C4"),
                        List.of(TypesOfPositions.COX, TypesOfPositions.SCULLING_ROWER),
                        List.of(new Tuple(startTime, endTime)))
                .getAvailableOffers();


        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void fetchFilteredCompetitions_aFewOffers_worksCorrectly() throws Exception {
        // Arrange
        TrainingOfferBuilder trainingBuilder = new TrainingOfferBuilder();
        trainingBuilder.setActive(true)
                .setBoatCertificate("C4")
                .setType(TypesOfActivities.TRAINING)
                .setPosition(TypesOfPositions.SCULLING_ROWER)
                .setStartTime(LocalDateTime.of(LocalDate.of(2022, 12, 12),
                        LocalTime.of(12, 00)))
                .setEndTime(LocalDateTime.of(LocalDate.of(2022, 12, 12),
                        LocalTime.of(14, 00)))
                .setOwnerId("ownerId")
                .setDescription("description")
                .setName("name");

        activityOfferRepository.save(trainingBuilder.build());

        CompetitionOfferBuilder competitionBuilder = new CompetitionOfferBuilder();
        competitionBuilder.setActive(true)
                .setFemale(true)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPosition(TypesOfPositions.COX)
                .setBoatCertificate("C4")
                .setType(TypesOfActivities.COMPETITION)
                .setOrganisation("Team Blue")
                .setPro(true)
                .setOwnerId("owner")
                .setDescription("description")
                .setName("name");

        activityOfferRepository.save(competitionBuilder.build());
        activityOfferRepository.save(competitionBuilder.build());


        competitionBuilder
                .setStartTime(LocalDateTime.of(LocalDate.of(2022, 12, 12),
                        LocalTime.of(12, 10)))
                .setEndTime(LocalDateTime.of(LocalDate.of(2022, 12, 12),
                        LocalTime.of(14, 15)));

        activityOfferRepository.save(competitionBuilder.build());

        competitionBuilder.setOrganisation("Team Red");
        activityOfferRepository.save(competitionBuilder.build());

        // Act
        LocalDateTime startAvailability1 = LocalDateTime.of(LocalDate.of(2022, 12, 12),
                LocalTime.of(10, 10));
        LocalDateTime endAvailability1 = LocalDateTime.of(LocalDate.of(2022, 12, 12),
                LocalTime.of(14, 15));

        LocalDateTime startAvailability2 = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0));
        LocalDateTime endAvailability2 = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(15, 10));

        List<CompetitionResponseModel> result = activityService
                .getFilteredCompetitionOffers(
                        "Team Blue",
                        true,
                        true,
                        List.of("C4"),
                        List.of(TypesOfPositions.COX, TypesOfPositions.SCULLING_ROWER),
                        List.of(new Tuple(startAvailability1, endAvailability1),
                                new Tuple(startAvailability2, endAvailability2))
                ).getAvailableOffers();

        // Assert
        assertThat(result.size()).isEqualTo(3);
    }
}
