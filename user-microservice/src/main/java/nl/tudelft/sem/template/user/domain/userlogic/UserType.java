package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.Getter;

@Getter
public enum UserType {
    PROFESSIONAL("professional"),
    AMATEUR("amateur");

    private String userType;

    UserType(String type) {
        this.userType = type;
    }
}
