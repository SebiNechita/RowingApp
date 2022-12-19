package nl.tudelft.sem.template.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "joinQueue")
@ToString
public class ActivityJoinQueueEntry {

    @Id
    @Getter
    @Column(name = "activityMatchId", nullable = false)
    private int activityMatchId;

    @Getter
    @Column(name = "enrolledUserId", nullable = false)
    private String enrolledUserId;
}
