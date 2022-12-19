package nl.tudelft.sem.template.common.models.activitymatch;

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
