import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.activitymatch.domain.TypesOfActivities;

@Data
public class MatchCreationRequestModel {

    private String ownerId;
    private String activityId;
    private String userId;
    private TypesOfActivities type;
}

