package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.activity.domain.TypesOfActivities;
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

    private String position;
    private boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private TypesOfActivities type;
    private String name;
    private String description;

    @BeforeEach
    void setup() {
        // Arrange
        this.position = "coach";
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
    }

    @Test
    public void createActivity_withValidData_worksCorrectly() throws Exception {
        // Act
        activityService.createTrainingOffer(position, isActive,
                startTime, endTime, ownerId, boatCertificate, type, name, description);

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

}
