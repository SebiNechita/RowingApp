package nl.tudelft.sem.template.common.models.activity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionCreationRequestModel extends TrainingCreationRequestModel {
    /**
     * Constructor.
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
     * @param isFemale        isFemale
     * @param isPro           isPro
     */
    public CompetitionCreationRequestModel(TypesOfPositions position, boolean isActive,
                                           LocalDateTime startTime, LocalDateTime endTime,
                                           String ownerId, String boatCertificate,
                                           TypesOfActivities type, String name, String description,
                                           String organisation, boolean isFemale, boolean isPro) {
        super(position, isActive, startTime, endTime, ownerId, boatCertificate, type, name, description);
        this.organisation = organisation;
        this.isFemale = isFemale;
        this.isPro = isPro;
    }


    private String organisation;
    private boolean isFemale;
    private boolean isPro;
}
