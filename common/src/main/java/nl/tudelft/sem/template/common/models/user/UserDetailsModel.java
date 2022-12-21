package nl.tudelft.sem.template.common.models.user;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

@AllArgsConstructor
@Data
public class UserDetailsModel {
    String netId;
    String gender;
    String organisation;
    boolean isPro;
    List<TypesOfPositions> positions;
    List<Tuple<LocalDateTime, LocalDateTime>> availabilities;
    List<String> certificates;
}
