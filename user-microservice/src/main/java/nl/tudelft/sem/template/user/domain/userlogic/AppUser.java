package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.user.domain.HasEvents;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public AppUser(NetId netId, HashedPassword password, String gender) {
        this.netId = netId;
        this.password = password;
        this.gender = gender;
        this.recordThat(new UserWasCreatedEvent(netId));
    }
}
