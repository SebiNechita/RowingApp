package nl.tudelft.sem.template.user.domain.userlogic.exceptions;

public class NetIdNotFoundException extends Exception{
    static final long serialVersionUID = 1L;

    public NetIdNotFoundException() {
        super("Net Id not found");
    }
}
