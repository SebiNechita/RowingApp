package nl.tudelft.sem.template.example.repositories;

import nl.tudelft.sem.template.example.domain.ActivityOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOfferRepository extends JpaRepository<ActivityOffer, Integer> {

}
