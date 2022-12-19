package nl.tudelft.sem.template.activitymatch.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.activitymatch.domain.TypesOfActivities;


@Data
@AllArgsConstructor
public class MatchCreationRequestModel {
    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;


}
