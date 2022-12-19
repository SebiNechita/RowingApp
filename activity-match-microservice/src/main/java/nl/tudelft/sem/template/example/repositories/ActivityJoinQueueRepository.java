package nl.tudelft.sem.template.example.repositories;

import nl.tudelft.sem.template.example.domain.ActivityJoinQueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityJoinQueueRepository extends JpaRepository<ActivityJoinQueueEntry, Integer> {
    /**
     * Find the list of enrolled users in an activity match by the ActivityMatch ID.
     *
     * @param activityMatchId the ID of the activity match.
     * @return a list of netIDs of users that are enrolled in this activity.
     */
    Optional<List<ActivityJoinQueueEntry>> findByActivityMatchId(int activityMatchId);
}
