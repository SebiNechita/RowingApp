package nl.tudelft.sem.template.common.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DDD value object representing a NetID in our domain.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NetId {
    private String netIdValue;

//    public NetId(String netId) {
//        // validate NetID
//        this.netIdValue = netId;
//    }

    @Override
    public String toString() {
        return netIdValue;
    }
}
