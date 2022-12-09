package nl.tudelft.sem.template.user.modules;

import lombok.Data;
import java.util.List;

@Data
public class SetAccountDetailsModel {
    String netId;
    String password;
    List<String> availabilities;
    List<String> certificates;
}
