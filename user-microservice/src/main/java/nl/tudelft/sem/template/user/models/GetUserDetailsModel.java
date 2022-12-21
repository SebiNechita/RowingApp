package nl.tudelft.sem.template.user.models;

import lombok.Data;
import nl.tudelft.sem.template.user.domain.userlogic.*;

import java.util.List;

@Data
public class GetUserDetailsModel {
    String netId;
    String gender;
    String userType;
    List<String> positions;
    List<String> availabilities;
    List<String> certificates;
    String organization;
}
