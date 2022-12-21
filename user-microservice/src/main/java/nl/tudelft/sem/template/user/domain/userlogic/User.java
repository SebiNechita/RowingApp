package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.*;
import nl.tudelft.sem.template.user.domain.HasEvents;
import nl.tudelft.sem.template.user.domain.userlogic.converters.GenderConverter;
import nl.tudelft.sem.template.user.domain.userlogic.events.UserWasCreatedEvent;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@NoArgsConstructor
public abstract class User extends HasEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Getter
    @Setter
    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Getter
    @Setter
    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @Getter
    @Setter
    @Column(name = "gender", nullable = false)
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Getter
    @Setter
    @NonNull
    @ElementCollection
    //@Convert(converter = TypeOfPositionConverter.class)
    @Column(name = "positions", nullable = false)
    private List<TypesOfPositions> positions;

    public User(@NonNull NetId netId,
                @NonNull HashedPassword password,
                @NonNull Gender gender,
                List<TypesOfPositions> positions) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.positions = positions;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (AmateurUser) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
