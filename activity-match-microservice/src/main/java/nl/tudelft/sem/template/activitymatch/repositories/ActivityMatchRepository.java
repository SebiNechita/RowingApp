package nl.tudelft.sem.template.activitymatch.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityMatchRepository extends JpaRepository<ActivityMatch, Integer> {

    /**
     * Method to find an ActivityMatch in the repository by its ID.
     *
     * @param id id
     * @return ActivityMatch
     */
    Optional<ActivityMatch> findById(int id);

    /**
     * Method to find an ActivityMatch in the repository by its Activity ID.
     *
     * @param activityId The ID of the activity.
     * @return ActivityMatch
     */
    @Query(value = "SELECT * FROM pendingactivities WHERE activity_id = ?1", nativeQuery = true)
    Optional<ActivityMatch> findByActivityId(int activityId);
}
