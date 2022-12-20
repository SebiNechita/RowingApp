package nl.tudelft.sem.template.activity.domain.exceptions;

public class InvalidOrganisationException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public InvalidOrganisationException(String parameter) {
        super(parameter + " is not a valid organisation name.");
    }
}
