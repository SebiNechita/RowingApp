package nl.tudelft.sem.template.user.domain.userlogic.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import nl.tudelft.sem.template.user.domain.userlogic.AmateurBuilder;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.entities.AmateurUser;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.entities.PositionEntity;
import nl.tudelft.sem.template.user.domain.userlogic.entities.User;
import nl.tudelft.sem.template.user.domain.userlogic.entities.UserCertificate;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.AvailabilityOverlapException;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.NetIdAlreadyInUseException;
import nl.tudelft.sem.template.user.domain.userlogic.exceptions.NetIdNotFoundException;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserPositionRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
public class AccountDetailsService {
    private final transient UserRepository userRepository;
    private final transient UserPositionRepository userPositionRepository;
    private final transient UserAvailabilityRepository availabilityRepository;
    private final transient UserCertificatesRepository userCertificatesRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository         the user repository
     * @param passwordHashingService the password encoder
     */
    public AccountDetailsService(UserRepository userRepository,
                                 PasswordHashingService passwordHashingService,
                                 UserAvailabilityRepository availabilityRepository,
                                 UserCertificatesRepository userCertificatesRepository,
                                 UserPositionRepository userPositionRepository) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
        this.availabilityRepository = availabilityRepository;
        this.userCertificatesRepository = userCertificatesRepository;
        this.userPositionRepository = userPositionRepository;
    }

    /**
     * The method for setting the details of a user account.
     *
     * @param netId          user's netId
     * @param password       user's password
     * @param gender         user's gender
     * @param availabilities user's availabilities
     * @param certificates   user's certificates
     */
    // TODO: check if organization exists in the Rowing Info
    public AmateurUser setAccountDetails(NetId netId,
                                         Password password,
                                         Gender gender,
                                         List<TypesOfPositions> positions,
                                         TreeMap<LocalDateTime, LocalDateTime> availabilities,
                                         List<String> certificates,
                                         String organization) throws Exception {

        dataValidation(netId, availabilities);
        AmateurUser user = setupAmateurUser(netId, password, gender, positions, availabilities, certificates, organization);

        System.out.println("Added " + userPositionRepository.findAllByNetId(netId).size());
        return user;
    }

    private AmateurUser setupAmateurUser(NetId netId,
                                         Password password,
                                         Gender gender,
                                         List<TypesOfPositions> positions,
                                         TreeMap<LocalDateTime, LocalDateTime> availabilities,
                                         List<String> certificates,
                                         String organization) {
        AmateurBuilder builder = new AmateurBuilder();

        // Hash password
        HashedPassword hashedPassword = passwordHashingService.hash(password);
        // Create new account
        builder.setNetId(netId)
                .setPassword(hashedPassword)
                .setGender(gender)
                .setCertificates(certificates)
                .setAvailabilities(availabilities)
                .setPositions(positions)
                .setOrganization(organization);

        AmateurUser user = builder.getUser();

        userRepository.save(user);
        saveAllProperties(builder);
        return user;
    }

    private void dataValidation(NetId netId, TreeMap<LocalDateTime, LocalDateTime> availabilities)
            throws NetIdAlreadyInUseException, AvailabilityOverlapException {
        if (!checkNetIdIsUnique(netId)) {
            throw new NetIdAlreadyInUseException(netId);
        }
        if (Availability.overlap(availabilities)) {
            throw new AvailabilityOverlapException();
        }
    }

    private void saveAllProperties(AmateurBuilder builder) {
        saveAvailabilities(builder);
        saveCertificates(builder);
        savePositions(builder);
    }

    private void savePositions(AmateurBuilder builder) {
        List<PositionEntity> positionsEntities = builder.getPositions();
        userPositionRepository.saveAll(positionsEntities);
    }

    private void saveCertificates(AmateurBuilder builder) {
        Set<UserCertificate> userCertificates = builder.getCertificates();
        userCertificatesRepository.saveAll(userCertificates);
    }

    private void saveAvailabilities(AmateurBuilder builder) {
        List<Availability> availabilitiesParsed = builder.getAvailabilities();
        availabilityRepository.saveAll(availabilitiesParsed);
    }

    /**
     * Retrieves the user's details with a NetId.
     *
     * @param netId netId of the user
     * @return UserDetailsModel
     * @throws Exception exception
     */
    public UserDetailsModel getAccountDetails(NetId netId) throws Exception {

        User user = getUser(netId);
        String netIdString = user.getNetId().toString();
        String gender = user.getGender().toString().toUpperCase(Locale.ENGLISH);
        String organisation = user.getOrganization();
        boolean isPro = !user.getClass().getSimpleName().equals("AmateurUser");
        List<TypesOfPositions> positions = getTypesOfPositions(netId);
        System.out.println(positions);
        List<String> certificates = getCertificates(netId);
        List<Tuple<LocalDateTime, LocalDateTime>> availabilities = getAvailabilities(netId);

        UserDetailsModel userModel = new UserDetailsModel(netIdString, gender, organisation,
                isPro, positions, availabilities, certificates);

        return userModel;
    }

    /**
     * Gets the availabilities.
     *
     * @param netId netID
     * @return list of availabilities
     */
    private List<Tuple<LocalDateTime, LocalDateTime>> getAvailabilities(NetId netId) {
        List<Tuple<LocalDateTime, LocalDateTime>> availabilities = availabilityRepository.findAllByNetId(netId)
                .stream()
                .map(x -> new Tuple<>(x.getStart(), x.getEnd()))
                .collect(Collectors.toList());
        return availabilities;
    }

    /**
     * Gets the certificates.
     *
     * @param netId netID
     * @return list of certificates
     */
    private List<String> getCertificates(NetId netId) {
        List<String> certificates = userCertificatesRepository.findAllByNetId(netId)
                .stream()
                .map(UserCertificate::getCertificate)
                .collect(Collectors.toList());
        return certificates;
    }

    /**
     * Gets the positions.
     *
     * @param netId netID
     * @return list of positions
     */
    private List<TypesOfPositions> getTypesOfPositions(NetId netId) {
        List<TypesOfPositions> positions = userPositionRepository.findAllByNetId(netId)
                .stream()
                .map(PositionEntity::getPosition)
                .collect(Collectors.toList());
        return positions;
    }

    /**
     * Gets the user.
     *
     * @param netId netID
     * @return user
     * @throws NetIdNotFoundException exception
     */
    private User getUser(NetId netId) throws NetIdNotFoundException {
        Optional<User> userOptional = userRepository.findByNetId(netId);
        if (userOptional.isEmpty()) {
            throw new NetIdNotFoundException();
        }
        User user = userOptional.get();
        return user;
    }

    /**
     * Check if the NetId is unique.
     *
     * @param netId netId
     * @return boolean
     */
    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }


}
