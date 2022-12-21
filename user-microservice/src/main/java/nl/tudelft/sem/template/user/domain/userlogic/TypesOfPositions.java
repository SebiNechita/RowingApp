package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
