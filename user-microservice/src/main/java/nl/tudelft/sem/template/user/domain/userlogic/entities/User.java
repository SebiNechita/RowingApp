package nl.tudelft.sem.template.user.domain.userlogic.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import nl.tudelft.sem.template.user.domain.HasEvents;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.converters.GenderConverter;
import nl.tudelft.sem.template.user.domain.userlogic.converters.HashedPasswordAttributeConverter;
import nl.tudelft.sem.template.user.domain.userlogic.converters.NetIdAttributeConverter;
import nl.tudelft.sem.template.user.domain.userlogic.events.UserWasCreatedEvent;

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
    @Column(name = "organization")
    private String organization;

    /**
     * Constructor for the User.
     *
     * @param netId        netId
     * @param password     password
     * @param gender       gender
     * @param organization organisation
     */
    public User(@NonNull NetId netId,
                @NonNull HashedPassword password,
                @NonNull Gender gender,
                String organization) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.organization = organization;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (AmateurUser) o;
        return id == user.id;
    }

    /**
     * Hash code for the User.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
