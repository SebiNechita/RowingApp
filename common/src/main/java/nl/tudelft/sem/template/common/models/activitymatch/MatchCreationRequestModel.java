package nl.tudelft.sem.template.common.models.activitymatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchCreationRequestModel {

    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;
}

