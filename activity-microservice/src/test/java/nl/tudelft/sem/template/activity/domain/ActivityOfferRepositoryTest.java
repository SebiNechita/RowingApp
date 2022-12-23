package nl.tudelft.sem.template.activity.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
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
public class ActivityOfferRepositoryTest {

    @Autowired
    private transient ActivityOfferRepository activityOfferRepository;

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

        // Add 4 trainings
        for (int i = 0; i < 4; i++) {
            TrainingOffer offer = new TrainingOffer(position, isActive, startTime, endTime, ownerId + i,
                    boatCertificate, type, name, description);
            activityOfferRepository.save(offer);
        }

        // Add 1 competition
        this.type = TypesOfActivities.COMPETITION;
        CompetitionOffer offer = new CompetitionOffer(position, isActive, startTime, endTime, ownerId,
                boatCertificate, type, name, description, organisation, isFemale, isPro);
        activityOfferRepository.save(offer);
    }

    @Test
    public void findByTypeTest() {
        assertThat(activityOfferRepository.findByType(TypesOfActivities.TRAINING).size()).isEqualTo(4);
    }
}
