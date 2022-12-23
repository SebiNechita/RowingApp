package nl.tudelft.sem.template.common.models.activity;

import lombok.Getter;

@Getter
public enum TypesOfPositions {

    COX("cox"),
    COACH("coach"),
    PORT_SIDE_ROWER("port_side_rower"),
    STARBOARD_ROWER("starboard_rower"),
    SCULLING_ROWER("sculling_rower");


    private final String type;

    TypesOfPositions(String type) {
        this.type = type;
    }

}
