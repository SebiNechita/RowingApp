package nl.tudelft.sem.template.common.models.activity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.common.domain.TrainingOffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTrainingsModel {
    private List<TrainingOffer> availableOffers;
}
