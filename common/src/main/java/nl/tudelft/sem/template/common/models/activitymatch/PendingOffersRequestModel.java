package nl.tudelft.sem.template.common.models.activitymatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingOffersRequestModel {
    private int activityId;
}
