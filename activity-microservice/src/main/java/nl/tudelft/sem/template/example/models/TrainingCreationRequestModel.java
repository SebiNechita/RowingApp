package nl.tudelft.sem.template.example.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.example.domain.TypesOfActivities;

@Data
@AllArgsConstructor
public class TrainingCreationRequestModel {
    private String position;
    private boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerId;
    private String boatCertificate;
    private TypesOfActivities type;


}
