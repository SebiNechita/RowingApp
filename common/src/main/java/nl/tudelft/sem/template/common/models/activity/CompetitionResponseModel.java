package nl.tudelft.sem.template.common.models.activity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionResponseModel {
    private int id;

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
}
