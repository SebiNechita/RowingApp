package nl.tudelft.sem.template.activitymatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participants")
@ToString
@EqualsAndHashCode
public class ActivityParticipant {
    @Id
    @Getter
    @Column(name = "activityMatchId", nullable = false)
    private int activityMatchId;

    @Getter
    @Column(name = "participantNetId", nullable = false)
    private String participantNetId;
}
