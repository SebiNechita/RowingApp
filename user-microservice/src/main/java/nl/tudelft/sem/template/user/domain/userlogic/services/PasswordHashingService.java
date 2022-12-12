package nl.tudelft.sem.template.user.domain.userlogic.services;

import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A DDD service for hashing passwords.
 */
public class PasswordHashingService {

    private final transient PasswordEncoder encoder;

    public PasswordHashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public HashedPassword hash(Password password) {
        return new HashedPassword(encoder.encode(password.toString()));
    }
}
