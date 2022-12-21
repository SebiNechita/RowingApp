package nl.tudelft.sem.template.common.models.activity;

import lombok.Data;

@Data
public class ParticipantIsEligibleRequestModel {
    private int activityOfferId;
    private String participantNetid;
}
