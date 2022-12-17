package nl.tudelft.sem.template.activity.domain.exceptions;

public class NotCorrectIntervalException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public NotCorrectIntervalException(String explanation) {
        super("Given time interval is not correct: " + explanation);
    }
}
