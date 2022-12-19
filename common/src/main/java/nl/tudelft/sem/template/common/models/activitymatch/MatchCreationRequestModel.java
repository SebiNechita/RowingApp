package nl.tudelft.sem.template.common.models.activitymatch;

import lombok.Data;
import nl.tudelft.sem.template.activitymatch.domain.TypesOfActivities;

@Data
public class MatchCreationRequestModel {

    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;
}

