package nl.tudelft.sem.template.common.models.activity_match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PendingOffersRequestModel {
    @Getter
    private int activityId;
}
