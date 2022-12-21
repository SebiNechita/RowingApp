package nl.tudelft.sem.template.user.domain.userlogic.services;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import nl.tudelft.sem.template.user.domain.userlogic.*;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.AvailabilityOverlapException;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.NetIdAlreadyInUseException;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import nl.tudelft.sem.template.user.models.GetAmateurUserDetailsModel;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
public class AmateurAccountDetailsService {
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
    public AmateurAccountDetailsService(UserRepository userRepository,
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
    public AmateurUser setAccountDetails(NetId netId,
                                         Password password,
                                         Gender gender,
                                         List<TypesOfPositions> positions,
                                         TreeMap<LocalDateTime, LocalDateTime> availabilities,
                                         List<String> certificates) throws Exception {

        boolean uniqueId = checkNetIdIsUnique(netId);
        if (!uniqueId) {
            throw new NetIdAlreadyInUseException(netId);
        }
        boolean availabilitiesOverlap = Availability.overlap(availabilities);
        if (availabilitiesOverlap) {
            throw new AvailabilityOverlapException();
        }
        // Hash password
        HashedPassword hashedPassword = passwordHashingService.hash(password);
        // Create new account
        AmateurBuilder builder = new AmateurBuilder();
        builder.setNetId(netId);
        builder.setPassword(hashedPassword);
        builder.setGender(gender);
        builder.setCertificates(certificates);
        builder.setAvailabilities(availabilities);
        builder.setPositions(positions);
        AmateurUser user = builder.getUser();
        List<Availability> availabilitiesParsed = builder.getAvailabilities();
        Set<UserCertificate> userCertificates = builder.getCertificates();
        userRepository.save(user);
        availabilityRepository.saveAll(availabilitiesParsed);
        userCertificatesRepository.saveAll(userCertificates);
        return user;
    }

    public GetAmateurUserDetailsModel getAccountDetails(NetId netId) throws Exception{
        boolean uniqueId = checkNetIdIsUnique(netId);
        if (!userRepository.existsByNetId(netId)) {
            throw new NetIdAlreadyInUseException(netId);
        }
        GetAmateurUserDetailsModel model = new GetAmateurUserDetailsModel();
        Optional<User> user = userRepository.findByNetId(netId);
        model.setNetId(netId.toString());
        model.setGender(user.get().getGender().getGender());
        List<String> availabilities = availabilityRepository.findAllByNetId(netId).stream().map(Availability::toString).collect(Collectors.toList());
        List<String> certificates = userCertificatesRepository.findAllByNetId(netId).stream().map(UserCertificate::toString).collect(Collectors.toList());
        model.setAvailabilities(availabilities);
        model.setCertificates(certificates);
        return model;
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }


}
