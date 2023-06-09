package nl.tudelft.sem.template.activity.repositories;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.activity.domain.ActivityOffer;
import nl.tudelft.sem.template.common.models.activity.TypesOfActivities;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@EntityScan(basePackages = "nl.tudelft.sem.template.activity.domain.ActivityOffer")
public interface ActivityOfferRepository extends JpaRepository<ActivityOffer, Integer> {

    /**
     * Method to find Activity in repository by its ID.
     *
     * @param id id
     * @return ActivityOffer
     */
    Optional<ActivityOffer> findById(int id);

    /**
     * Method to find all Activities by their type.
     *
     * @param type type of activity
     * @return list of all such activities
     */
    List<ActivityOffer> findByType(TypesOfActivities type);
}
