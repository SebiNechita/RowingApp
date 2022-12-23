package nl.tudelft.sem.template.user.domain.userlogic.repos;

import java.util.List;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAvailabilityRepository extends JpaRepository<Availability, String> {

    List<Availability> findAllByNetId(NetId s);
}
