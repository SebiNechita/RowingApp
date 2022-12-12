package nl.tudelft.sem.template.user.domain.userlogic.services;

import nl.tudelft.sem.template.user.controllers.UserController;
import nl.tudelft.sem.template.user.domain.userlogic.*;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.NetIdAlreadyInUseException;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

/**
 * A DDD service for registering a new user.
 */
@Service
public class AccountDetailsService {
    private final transient UserRepository userRepository;
    private final transient UserAvailabilityRepository availabilityRepository;
    private final transient UserCertificatesRepository userCertificatesRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     * @param passwordHashingService the password encoder
     */
    public AccountDetailsService(UserRepository userRepository,
                                 PasswordHashingService passwordHashingService,
                                 UserAvailabilityRepository availabilityRepository,
                                 UserCertificatesRepository userCertificatesRepository) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
        this.availabilityRepository = availabilityRepository;
        this.userCertificatesRepository = userCertificatesRepository;
    }

    public AppUser setAccountDetails(NetId netId,
                                     Password password,
                                     String gender,
                                     TreeMap<LocalDateTime, LocalDateTime> availabilities,
                                     List<String> certificates) throws Exception {
        if (checkNetIdIsUnique(netId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);
            // Create new account
            AppUser user = new AppUser(netId, hashedPassword, gender);
            userRepository.save(user);
            availabilityRepository.save(netId, availabilities);



            return user;
        }

        throw new NetIdAlreadyInUseException(netId);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
