package nl.tudelft.sem.template.common.models.activity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCompetitionsModel {
    private List<CompetitionResponseModel> availableOffers;
}
