package nl.tudelft.sem.template.user.domain.userlogic;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

/**
 * A DDD service for registering a new user.
 */
@Service
public class SetAccountDetailsService {
    private final transient UserRepository userRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     * @param passwordHashingService the password encoder
     */
    public SetAccountDetailsService(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    public AppUser setAccountDetails(NetId netId,
                                     Password password,
                                     String gender,
                                     TreeMap<LocalTime, LocalTime> availabilities,
                                     List<UserCertificate> certificates) throws Exception {
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
