package nl.tudelft.sem.template.user.domain.userlogic.repos;

import nl.tudelft.sem.template.user.domain.userlogic.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAvailabilityRepository extends JpaRepository<Availability, String> {

    List<Availability> findAllByNetId(NetId s);
}
