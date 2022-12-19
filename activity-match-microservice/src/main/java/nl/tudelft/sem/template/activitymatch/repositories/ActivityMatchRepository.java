package nl.tudelft.sem.template.activitymatch.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.activitymatch.domain.ActivityMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityMatchRepository extends JpaRepository<ActivityMatch, Integer> {

    /**
     * Method to find Activity in repository by its ID.
     *
     * @param id id
     * @return ActivityMatch
     */
    Optional<ActivityMatch> findById(int id);
}
