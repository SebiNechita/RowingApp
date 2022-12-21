package nl.tudelft.sem.template.activitymatch.domain;

import javax.persistence.*;

import lombok.*;
import nl.tudelft.sem.template.activitymatch.repositories.ActivityJoinQueueRepository;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "joinQueue")
@ToString
@EqualsAndHashCode
public class ActivityJoinQueueEntry {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @Column(name = "activityMatchId", nullable = false)
    private int activityMatchId;

    @Getter
    @Column(name = "enrolledUserId", nullable = false)
    private String enrolledUserNetId;

    /**
     * Constructs an ActivityJoinQueueEntry without an ID.
     *
     * @param activityMatchId The ID of the activity match.
     * @param enrolledUserNetId The NetID of the user in the join queue.
     */
    public ActivityJoinQueueEntry(int activityMatchId, @NonNull String enrolledUserNetId) {
        this.activityMatchId = activityMatchId;
        this.enrolledUserNetId = enrolledUserNetId;
    }
}
