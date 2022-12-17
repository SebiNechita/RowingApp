package nl.tudelft.sem.template.user.domain.userlogic;

public enum Gender {
    MALE("male"),
    FEMALE("female");

    private String gender;
    Gender(String gender) {
        this.gender = gender;
    }
}
