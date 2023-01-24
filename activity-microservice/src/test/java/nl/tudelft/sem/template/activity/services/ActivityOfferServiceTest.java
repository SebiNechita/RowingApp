package nl.tudelft.sem.template.activity.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOffer;
import nl.tudelft.sem.template.activity.domain.CompetitionOfferBuilder;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.ParticipantIsEligibleRequestModel;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockDataValidation", "mockActivityOfferRepository", "mockActivityClient", "mockUserModelParser"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityOfferServiceTest {

    @Autowired
    private transient ActivityOfferService activityOfferService;
    @Autowired
    private transient ActivityOfferRepository mockActivityOfferRepository;
    @Autowired
    private transient ActivityClient mockActivityClient;
    @Autowired
    private transient UserModelParser mockUserModelParser;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private String name;
    private String description;
    private String organisation;
    private boolean isPro;
    private String authToken;

    /**
     * Setting up the objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0, 0));
        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(12, 0, 0));
        this.ownerId = "papiez";
        this.boatCertificate = "C4";
        this.name = "Team Blue Training";
        this.description = "Looking for a new member";
        this.organisation = "teamAlpha";
        this.isPro = true;
        this.authToken = "pwd";
    }

    @Test
    public void participantIsEligible_works_correctly() throws Exception {

        CompetitionOfferBuilder competitionBuilder = new CompetitionOfferBuilder();
        ActivityOffer competition = competitionBuilder.setActive(true)
                .setFemale(true)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPosition(TypesOfPositions.COX)
                .setBoatCertificate(boatCertificate)
                .setType(TypesOfActivities.COMPETITION)
                .setOwnerId(ownerId)
                .setDescription(description)
                .setName(name)
                .setOrganisation(organisation)
                .setPro(isPro)
                .build();
        when(mockActivityOfferRepository.findById(1)).thenReturn(Optional.of(competition));
        when(mockActivityClient.getUserDetails(anyString(), anyString())).thenReturn(new ResponseEntitySimulator());

        String modelNetId = "mihai";
        String modelGender = "FEMALE";
        String modelOrganisation = organisation;
        boolean modelPro = true;
        List<TypesOfPositions> modelPositions = Arrays.asList(TypesOfPositions.COX, TypesOfPositions.COACH);
        List<Tuple<LocalDateTime, LocalDateTime>> modelAvailabilities = List.of(
                new Tuple<>((LocalDateTime.of(LocalDate.of(2022, 1, 8),
                        LocalTime.of(10, 0, 0))), (LocalDateTime.of(LocalDate.of(2022, 1, 8),
                        LocalTime.of(12, 0, 0)))));
        List<String> modelCertificates = List.of("C4");
        UserDetailsModel userDetailsModel = new UserDetailsModel(modelNetId,
                modelGender,
                modelOrganisation,
                modelPro,
                modelPositions,
                modelAvailabilities,
                modelCertificates);
        when(mockUserModelParser.getModel(any())).thenReturn(userDetailsModel);
        when(mockActivityOfferRepository.findAll()).thenReturn(List.of(competition));
        ParticipantIsEligibleRequestModel model = new ParticipantIsEligibleRequestModel(1, "mihai");
        assertTrue(activityOfferService.participantIsEligible(model, authToken));
    }

}
