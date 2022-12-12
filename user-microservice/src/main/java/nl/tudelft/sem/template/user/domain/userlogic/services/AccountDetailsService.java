package nl.tudelft.sem.template.user.domain.userlogic.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import nl.tudelft.sem.template.user.domain.userlogic.AppUser;
import nl.tudelft.sem.template.user.domain.userlogic.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.UserCertificate;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.AvailabilityOverlapException;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.NetIdAlreadyInUseException;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import org.springframework.stereotype.Service;

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

    /**
     * The method for setting the details of a user account.
     *
     * @param netId  user's netId
     * @param password user's password
     * @param gender user's gender
     * @param availabilities user's availabilities
     * @param certificates user's certificates
     */
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
            if (!Availability.overlap(availabilities)) {
                userRepository.save(user);
                for (Map.Entry<LocalDateTime, LocalDateTime> entry : availabilities.entrySet()) {
                    LocalDateTime start = entry.getKey();
                    LocalDateTime end = entry.getValue();
                    Availability availability = new Availability(netId, start, end);

                    availabilityRepository.save(availability);
                }
                Set<String> noDuplicateCertificates = new HashSet<>(certificates);
                for (String certificate : noDuplicateCertificates) {
                    UserCertificate userCertificate = new UserCertificate(netId, certificate);
                    userCertificatesRepository.save(userCertificate);
                }
            } else {
                throw new AvailabilityOverlapException();
            }
            return user;
        }
        throw new NetIdAlreadyInUseException(netId);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
