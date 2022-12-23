package nl.tudelft.sem.template.user.domain.userlogic.repos;

import java.util.List;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.entities.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPositionRepository extends JpaRepository<PositionEntity, String> {

    /**
     * Get all the position for the User with this NetId.
     *
     * @param s netId of the User
     * @return list of positions
     */
    List<PositionEntity> findAllByNetId(NetId s);
}
