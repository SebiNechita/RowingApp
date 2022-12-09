package nl.tudelft.sem.template.user.domain.userlogic;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A DDD service for registering a new user.
 */
@Service
public class SetAccountDetailsService {
    private final transient UserRepository userRepository;

    public SetAccountDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser setAccountDetailsUser(NetId netId,
                                         Password password,
                                         String gender,
                                         List<Availability> availabilities,
                                         List<Certificates> certificates) {
        if (checkNetIdIsUnique(netId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            AppUser user = new AppUser(netId, hashedPassword, gender, certificates);
            userRepository.save(user);

            return user;
        }

        throw new NetIdAlreadyInUseException(netId);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
