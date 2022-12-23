package nl.tudelft.sem.template.user.models;

import java.util.List;
import lombok.Data;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;

@Data
public class AmateurSetAccountDetailsModel {
    String netId;
    String password;
    String gender;
    List<TypesOfPositions> positions;
    List<Tuple<String, String>> availabilities;
    List<String> certificates;
    String organization;
}
