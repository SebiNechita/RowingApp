package nl.tudelft.sem.template.activity.domain.exceptions;

public class InvalidCertificateException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public InvalidCertificateException(String parameter) {
        super(parameter + " is not a valid certificate name.");
    }
}
