package nl.tudelft.sem.template.user.domain.userlogic.services;

import java.time.LocalDateTime;
import java.util.List;
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
        builder.setOrganization(organization);
        AmateurUser user = builder.getUser();
        List<Availability> availabilitiesParsed = builder.getAvailabilities();
        Set<UserCertificate> userCertificates = builder.getCertificates();
        Set<PositionEntity> positionsEntities = builder.getPositions();
        userRepository.save(user);
        availabilityRepository.saveAll(availabilitiesParsed);

        userCertificatesRepository.saveAll(userCertificates);
        userPositionRepository.saveAll(positionsEntities);
        return user;
    }

    public UserDetailsModel getAccountDetails(NetId netId) throws Exception {
        boolean uniqueId = checkNetIdIsUnique(netId);
        if (!userRepository.existsByNetId(netId)) {
            throw new NetIdAlreadyInUseException(netId);
        }
        User user = userRepository.findByNetId(netId).get();

        String netIdString = user.getNetId().toString();
        String gender = user.getGender().toString().toUpperCase();
        String organisation = user.getOrganization();
        boolean isPro = !user.getClass().getSimpleName().equals("AmateurUser");
        List<TypesOfPositions> positions = userPositionRepository.findAllByNetId(netId)
                .stream()
                .map(PositionEntity::getPosition)
                .collect(Collectors.toList());
        List<String> certificates = userCertificatesRepository.findAllByNetId(netId)
                .stream()
                .map(UserCertificate::toString)
                .collect(Collectors.toList());
        List<Tuple<LocalDateTime, LocalDateTime>> availabilities = availabilityRepository.findAllByNetId(netId)
                .stream()
                .map(x -> new Tuple<>(x.getStart(), x.getEnd()))
                .collect(Collectors.toList());

        //        GetUserDetailsModel model = new GetUserDetailsModel();
        //        model.setNetId(netId.toString());
        //        model.setUserType(user.get().getClass().getSimpleName());
        //        model.setOrganization(user.get().getOrganization());
        //        model.setGender(user.get().getGender().getGender());
        //        List<String> availabilities = availabilityRepository.findAllByNetId(netId)
        //          .stream().map(Availability::toString).collect(Collectors.toList());
        //        List<String> certificates = userCertificatesRepository.findAllByNetId(netId)
        //          .stream().map(UserCertificate::toString).collect(Collectors.toList());
        //        List<String> positions = userPositionRepository.findAllByNetId(netId)
        //          .stream().map(PositionEntity::toString).collect(Collectors.toList());
        //        model.setAvailabilities(availabilities);
        //        model.setCertificates(certificates);

        UserDetailsModel userModel = new UserDetailsModel(netIdString, gender, organisation,
                isPro, positions, availabilities, certificates);

        return userModel;
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }


}
