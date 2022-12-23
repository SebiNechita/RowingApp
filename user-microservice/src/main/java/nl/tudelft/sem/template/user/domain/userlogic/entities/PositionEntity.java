package nl.tudelft.sem.template.user.domain.userlogic.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.converters.NetIdAttributeConverter;
import nl.tudelft.sem.template.user.domain.userlogic.converters.TypeOfPositionConverter;

@Entity
@Table(name = "positions")
@Getter
@NoArgsConstructor
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Getter
    @Column(name = "position", nullable = false)
    @Convert(converter = TypeOfPositionConverter.class)
    private TypesOfPositions position;

    public PositionEntity(NetId netId, TypesOfPositions position) {
        this.netId = netId;
        this.position = position;
    }

    public String toString() {
        return this.position.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionEntity that = (PositionEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
