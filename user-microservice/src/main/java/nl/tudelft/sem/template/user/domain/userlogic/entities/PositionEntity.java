package nl.tudelft.sem.template.user.domain.userlogic.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.converters.NetIdAttributeConverter;
import nl.tudelft.sem.template.user.domain.userlogic.converters.TypeOfPositionConverter;
import nl.tudelft.sem.template.user.domain.userlogic.TypesOfPositions;

import javax.persistence.*;
import java.util.Objects;

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
    NetId netId;

    @Column(name = "position", nullable = false)
    @Convert(converter = TypeOfPositionConverter.class)
    TypesOfPositions position;

    public PositionEntity(NetId netId, TypesOfPositions position) {
        this.netId = netId;
        this.position = position;
    }

    public String toString() {
        return this.position.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionEntity that = (PositionEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
