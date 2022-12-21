package nl.tudelft.sem.template.user.models;

import lombok.Data;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import nl.tudelft.sem.template.user.domain.userlogic.TypesOfPositions;
import nl.tudelft.sem.template.user.domain.userlogic.UserType;

import java.util.List;

@Data
public class AmateurSetAccountDetailsModel {
    String netId;
    String password;
    String gender;
    List<TypesOfPositions> positions;
    List<Tuple<String, String>> availabilities;
    List<String> certificates;
}
