package nl.tudelft.sem.template.activity.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.activity.domain.TrainingOffer;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTrainingsModel {
    private List<TrainingOffer> availableTrainings;
}
