package nl.tudelft.sem.template.common.models.activity_match;

import lombok.Getter;

@Getter
public enum TypesOfActivities {
    TRAINING("training"),
    COMPETITION("competition");

    private final String type;

    TypesOfActivities(String type) {
        this.type = type;
    }
}
