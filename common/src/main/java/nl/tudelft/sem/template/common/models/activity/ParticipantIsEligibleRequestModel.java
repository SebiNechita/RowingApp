package nl.tudelft.sem.template.common.models.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantIsEligibleRequestModel {
    private int activityOfferId;
    private String participantNetid;
}
