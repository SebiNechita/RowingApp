package nl.tudelft.sem.template.user.modules;

import java.util.List;
import lombok.Data;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;

@Data
public class SetAccountDetailsModel {
    String netId;
    String password;
    String gender;
    List<Tuple<String, String>> availabilities;
    List<String> certificates;
}
