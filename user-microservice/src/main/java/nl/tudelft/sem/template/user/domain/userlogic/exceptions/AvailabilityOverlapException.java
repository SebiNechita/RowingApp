package nl.tudelft.sem.template.user.domain.userlogic.exceptions;

public class AvailabilityOverlapException extends Exception {

    static final long serialVersionUID = 1L;

    public AvailabilityOverlapException() {
        super("Availabilities Overlap");
    }
}
