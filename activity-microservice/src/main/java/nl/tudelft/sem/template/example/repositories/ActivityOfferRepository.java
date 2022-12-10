package nl.tudelft.sem.template.example.repositories;

import java.util.Optional;
import nl.tudelft.sem.template.example.domain.ActivityOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOfferRepository extends JpaRepository<ActivityOffer, Integer> {

    /**
     * Method to find Actividy in repository by its ID.
     *
     * @param id id
     * @return ActivityOffer
     */
    Optional<ActivityOffer> findById(int id);
}
