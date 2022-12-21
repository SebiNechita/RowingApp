package nl.tudelft.sem.template.common.models.activity;

import lombok.Getter;

@Getter
public enum TypesOfPositions {

    COX("cox"),
    COACH("coach"),
    PORT_SIDE_ROWER("port side rower"),
    STARBOARD_ROWER("starboard rower"),
    SCULLING_ROWER("sculling rower");


    private final String type;

    TypesOfPositions(String type) {
        this.type = type;
    }

}
