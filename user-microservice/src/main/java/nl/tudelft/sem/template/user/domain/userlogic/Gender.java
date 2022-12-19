package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String gender;
    Gender(String gender) {
        this.gender = gender;
    }
}
