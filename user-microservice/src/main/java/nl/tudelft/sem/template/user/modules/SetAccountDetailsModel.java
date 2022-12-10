package nl.tudelft.sem.template.user.modules;

import lombok.Data;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;

import java.util.List;
import java.util.Map;

@Data
public class SetAccountDetailsModel {
    String netId;
    String password;
    String gender;
    List<Tuple<String, String>> availabilities;
    List<String> certificates;
}
