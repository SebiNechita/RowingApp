package nl.tudelft.sem.template.activitymatch.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.activitymatch.domain.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Integer> {
    /**
     * Find the participant for a given activity match ID.
     *
     * @param activityMatchId the ID of the activity match.
     * @return the ActivityParticipant for the activity match, or null if there is no participant yet.
     */
    Optional<ActivityParticipant> findByActivityMatchId(int activityMatchId);
}
