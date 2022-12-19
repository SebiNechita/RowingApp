package nl.tudelft.sem.template.common.models.activitymatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class MatchCreationRequestModel {

    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;
}

