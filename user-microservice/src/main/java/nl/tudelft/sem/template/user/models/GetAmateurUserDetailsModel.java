package nl.tudelft.sem.template.user.models;

import lombok.Data;
import nl.tudelft.sem.template.user.domain.userlogic.*;

import java.util.List;

@Data
public class GetAmateurUserDetailsModel {
    String netId;
    String gender;
    //List<TypesOfPositions> positions;
    List<String> availabilities;
    List<String> certificates;
}
