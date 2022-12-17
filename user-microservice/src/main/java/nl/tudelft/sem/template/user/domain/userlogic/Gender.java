package nl.tudelft.sem.template.user.domain.userlogic;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female");

    private String gender;
    Gender(String gender) {
        this.gender = gender;
    }
}
