package nl.tudelft.sem.template.example.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.example.domain.ActivityMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityMatchRepository extends JpaRepository<ActivityMatch, Integer> {

    /**
     * Method to find Activity in repository by its ID.
     *
     * @param id id
     * @return ActivityOffer
     */
    Optional<ActivityMatch> findById(int id);
}
