package nl.tudelft.sem.template.example.repositories;

import nl.tudelft.sem.template.example.domain.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Integer> {
    /**
     * Find the participant for a given activity match ID.
     *
     * @param activityMatchId the ID of the activity match.
     * @return the ActivityParticipant for the activity match, or null if there is no participant yet.
     */
    Optional<ActivityParticipant> findByActivityMatchId(int activityMatchId);
}
