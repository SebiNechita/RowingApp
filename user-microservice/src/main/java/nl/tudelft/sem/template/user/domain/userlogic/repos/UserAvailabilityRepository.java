package nl.tudelft.sem.template.user.domain.userlogic.repos;

import nl.tudelft.sem.template.user.domain.userlogic.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAvailabilityRepository extends JpaRepository<Availability, String> {
}
