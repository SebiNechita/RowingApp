package nl.tudelft.sem.template.user.domain.userlogic;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.user.domain.HasEvents;
import nl.tudelft.sem.template.user.domain.userlogic.events.UserWasCreatedEvent;

import java.util.Objects;


@Entity
@Table(name = "users")
@NoArgsConstructor
public class AppUser extends HasEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @Column(name = "gender", nullable = false)
    private String gender;

    /**
     * Create new application user.
     *
     * @param netId The NetId for the new user
     * @param password The password for the new user
     * @param gender The gender of the user
     */
    public AppUser(NetId netId, HashedPassword password, String gender) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    public NetId getNetId() {
        return netId;
    }

    public HashedPassword getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
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
        AppUser appUser = (AppUser) o;
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netId);
    }
}
