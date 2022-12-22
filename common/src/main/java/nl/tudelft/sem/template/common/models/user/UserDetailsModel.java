package nl.tudelft.sem.template.common.models.user;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetailsModel {
    String netId;
    String gender;
    String organisation;
    List<TypesOfPositions> positions;
    List<Tuple<LocalDateTime, LocalDateTime>> availabilities;
    List<String> certificates;
    boolean pro;
}
