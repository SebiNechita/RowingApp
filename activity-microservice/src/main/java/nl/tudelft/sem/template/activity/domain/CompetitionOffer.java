package nl.tudelft.sem.template.activity.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;

@Entity
@NoArgsConstructor
@ToString
public class CompetitionOffer extends ActivityOffer {

    @NonNull
    @Column(name = "organisation", nullable = true)
    @Getter
    private String organisation;

    @Column(name = "gender", nullable = true)
    @Getter
    private boolean isFemale;

    @Column(name = "experience", nullable = true)
    @Getter
    private boolean isPro;

    /**
     * Initialises a new CompetitionOffer without an Id.
     *
     * @param position        position
     * @param isActive        isActive
     * @param startTime       startTime
     * @param endTime         endTime
     * @param ownerId         ownerId
     * @param boatCertificate boatCertificate
     * @param type            type
     * @param name            name
     * @param description     description
     * @param organisation    organisation
     * @param isFemale        boolean is the competition for females
     * @param isPro           boolean is the competition for experienced rowers
     */
    public CompetitionOffer(@NonNull TypesOfPositions position,
                            boolean isActive,
                            @NonNull LocalDateTime startTime,
                            @NonNull LocalDateTime endTime,
                            @NonNull String ownerId,
                            String boatCertificate,
                            TypesOfActivities type,
                            @NonNull String name,
                            @NonNull String description,
                            @NonNull String organisation,
                            boolean isFemale,
                            boolean isPro) {
        super(position, isActive, startTime, endTime, ownerId, boatCertificate, type, name, description);
        this.organisation = organisation;
        this.isFemale = isFemale;
        this.isPro = isPro;
    }
}
