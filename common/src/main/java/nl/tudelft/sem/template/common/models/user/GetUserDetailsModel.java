package nl.tudelft.sem.template.common.models.user;

import lombok.Data;

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
