package nl.tudelft.sem.template.activity.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompetitionCreationRequestModel extends TrainingCreationRequestModel {
    private String organisation;
    private boolean isFemale;
    private boolean isPro;
}
