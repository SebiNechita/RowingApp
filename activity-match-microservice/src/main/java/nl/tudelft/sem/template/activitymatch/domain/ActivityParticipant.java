package nl.tudelft.sem.template.activitymatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participants")
@ToString
public class ActivityParticipant {
    @Id
    @Getter
    @Column(name = "activityMatchId", nullable = false)
    private int activityMatchId;

    @Getter
    @Column(name = "participantNetId", nullable = false)
    private String participantNetId;
}
