package nl.tudelft.sem.template.user.domain.userlogic.exceptions;

public class AvailabilityOverlapException extends Exception{

    public AvailabilityOverlapException() {
        super("Availabilities Overlap");
    }
}
