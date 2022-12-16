package nl.tudelft.sem.template.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.example.domain.TypesOfActivities;

import java.time.LocalDateTime;




@Data
@AllArgsConstructor
public class MatchCreationRequestModel {
    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;


}
