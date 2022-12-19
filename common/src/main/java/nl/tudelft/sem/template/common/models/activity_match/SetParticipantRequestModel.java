package nl.tudelft.sem.template.common.models.activity_match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class SetParticipantRequestModel {

    public int activityId;
    public String participantNetId;
}
