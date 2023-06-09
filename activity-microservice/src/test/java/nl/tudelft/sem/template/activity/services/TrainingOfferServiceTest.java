package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import org.assertj.core.api.ThrowableAssert;
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
public class TrainingOfferServiceTest {

    @Autowired
    private transient TrainingOfferService activityService;
    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;
    @Autowired
    private transient DataValidation mockDataValidation;

    private TypesOfPositions position;

    private Map<TypesOfPositions, Integer> positions;
    private boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private TypesOfActivities type;
    private String name;
    private String description;
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
        this.ownerId = "testUser";
        this.boatCertificate = "C4";
        this.type = TypesOfActivities.TRAINING;
        this.name = "Team Blue Training";
        this.description = "Pumping the iron all day long";
        this.positions = new HashMap<>();
        this.positions.put(TypesOfPositions.COX, 2);
        this.positions.put(TypesOfPositions.COACH, 1);

        when(mockDataValidation.validateOrganisation(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateCertificate(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateData(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateNameAndDescription(any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateTime(any(), any())).thenCallRealMethod();
    }

    @Test
    public void createActivity_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createTrainingOffer(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description, authToken);

        // Assert
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

    //    @Test
    //    public void createActivity_withEmptyName_throwsException() throws Exception {
    //        // Arrange
    //        this.name = "";
    //
    //        // Act
    //        ThrowableAssert.ThrowingCallable action = () -> activityService
    //                .createTrainingOffer(position, isActive, startTime, endTime, ownerId,
    //                        boatCertificate, type, name, description, authToken);
    //
    //        // Assert
    //        assertThatExceptionOfType(EmptyStringException.class)
    //                .isThrownBy(action);
    //    }

    //    @Test
    //    public void createActivity_withEmptyDescription_throwsException() {
    //        // Arrange
    //        this.description = "";
    //
    //        // Act
    //        ThrowableAssert.ThrowingCallable action = () -> activityService
    //                .createTrainingOffer(position, isActive, startTime, endTime, ownerId,
    //                        boatCertificate, type, name, description, authToken);
    //
    //        // Assert
    //        assertThatExceptionOfType(EmptyStringException.class)
    //                .isThrownBy(action);
    //    }

    //    @Test
    //    public void createActivity_withInvalidStartEndTime_throwsException() {
    //        // Arrange
    //        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
    //                LocalTime.of(12, 0, 0));
    //        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
    //                LocalTime.of(10, 0, 0));
    //
    //        // Act
    //        ThrowableAssert.ThrowingCallable action = () -> activityService
    //                .createTrainingOffer(position, isActive, startTime, endTime, ownerId,
    //                        boatCertificate, type, name, description, authToken);
    //
    //        // Assert
    //        assertThatExceptionOfType(NotCorrectIntervalException.class)
    //                .isThrownBy(action);
    //    }

    @Test
    public void createManyActivities_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createManyTrainingOffers(positions, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description, authToken);

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
}
