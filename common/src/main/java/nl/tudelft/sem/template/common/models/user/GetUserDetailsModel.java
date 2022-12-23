package nl.tudelft.sem.template.common.models.user;

import java.util.List;
import lombok.Data;

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
