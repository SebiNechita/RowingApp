package nl.tudelft.sem.template.rowinginfo.domain.exceptions;

public class EmptyStringException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public EmptyStringException(String parameter) {
        super(parameter + " can't be an empty string.");
    }
}
