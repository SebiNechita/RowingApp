package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
import nl.tudelft.sem.template.activity.domain.TypesOfPositions;
import nl.tudelft.sem.template.activity.models.ManyTrainingsCreationRequestModel;
import nl.tudelft.sem.template.activity.models.TrainingCreationRequestModel;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
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
public class ActivityOfferServiceTest {

    @Autowired
    private transient ActivityOfferService activityService;
    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;

    private TrainingCreationRequestModel requestModel;

    private ManyTrainingsCreationRequestModel manyRequestsModel;
    private TypesOfPositions position;

    private Map<TypesOfPositions, Integer> positions;
    private boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private TypesOfActivities type;

    @BeforeEach
    void setup() {
        // Arrange
        this.position = TypesOfPositions.COACH;
        this.isActive = true;
        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0, 0));
        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(12, 0, 0));
        this.ownerId = "testUser";
        this.boatCertificate = "C4";
        this.type = TypesOfActivities.TRAINING;
        this.positions = new HashMap<>();
        this.positions.put(TypesOfPositions.COX, 2);
        this.positions.put(TypesOfPositions.COACH, 1);

        this.requestModel = new TrainingCreationRequestModel(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type);
        this.manyRequestsModel = new ManyTrainingsCreationRequestModel(positions, isActive,
                startTime, endTime, ownerId, boatCertificate, type);
    }

    @Test
    public void createActivity_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createTrainingOffer(requestModel);

        // Assert
        ActivityOffer activityOffer = activityOfferRepository.findById(1).orElseThrow();

        assertThat(activityOffer.getPosition()).isEqualTo(position);
        assertThat(activityOffer.isActive()).isEqualTo(isActive);
        assertThat(activityOffer.getStartTime()).isEqualTo(startTime);
        assertThat(activityOffer.getEndTime()).isEqualTo(endTime);
        assertThat(activityOffer.getOwnerId()).isEqualTo(ownerId);
        assertThat(activityOffer.getBoatCertificate()).isEqualTo(boatCertificate);
        assertThat(activityOffer.getType()).isEqualTo(type);
    }

    @Test
    public void createManyActivities_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createManyTrainingOffers(manyRequestsModel);

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
            }
        }
    }

}
