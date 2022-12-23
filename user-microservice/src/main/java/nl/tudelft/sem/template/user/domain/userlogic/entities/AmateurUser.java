package nl.tudelft.sem.template.user.domain.userlogic.entities;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;

@NoArgsConstructor
@Entity
@DiscriminatorValue("amateur")
@Getter
public class AmateurUser extends User {
    /**
     * Create new application user.
     *
     * @param netId The NetId for the new user
     * @param password The password for the new user
     * @param gender The gender of the user
     */
    public AmateurUser(@NonNull NetId netId,
                       @NonNull HashedPassword password,
                       @NonNull Gender gender,
                       String organization) {
        super(netId, password, gender, organization);
    }

    /**
     * Equality is only based on the identifier.
     */


}
