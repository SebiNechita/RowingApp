package nl.tudelft.sem.template.user.domain.userlogic.repos;

import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.entities.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPositionRepository extends JpaRepository<PositionEntity, String> {

    List<PositionEntity> findAllByNetId(NetId s);
}
