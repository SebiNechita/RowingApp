package nl.tudelft.sem.template.user.domain.userlogic;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

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
                       List<TypesOfPositions> positions) {
        super(netId, password, gender, positions);
    }

    /**
     * Equality is only based on the identifier.
     */


}
