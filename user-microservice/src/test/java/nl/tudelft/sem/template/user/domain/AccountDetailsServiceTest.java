package nl.tudelft.sem.template.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.entities.AmateurUser;
import nl.tudelft.sem.template.user.domain.userlogic.entities.Availability;
import nl.tudelft.sem.template.user.domain.userlogic.entities.User;
import nl.tudelft.sem.template.user.domain.userlogic.entities.UserCertificate;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import nl.tudelft.sem.template.user.domain.userlogic.services.AccountDetailsService;
import nl.tudelft.sem.template.user.domain.userlogic.services.PasswordHashingService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockUserRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountDetailsServiceTest {

    @Autowired
    private transient AccountDetailsService accountDetailsService;

    @Autowired
    private transient UserAvailabilityRepository availabilityRepository;

    @Autowired
    private transient UserCertificatesRepository userCertificatesRepository;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;
    @Autowired
    private transient UserRepository mockUserRepository;


    private TreeMap<LocalDateTime, LocalDateTime> availabilities;
    private List<Tuple<LocalDateTime, LocalDateTime>> availabilitiesList;

    @BeforeEach
    public void setUp() {
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T13:00");

        availabilities = new TreeMap<>();
        availabilities.put(dateOneIntervalOne, dateTwoIntervalOne);

        availabilitiesList = List.of(new Tuple<>(dateOneIntervalOne, dateTwoIntervalOne));
    }

    //    @Test
    //    void setAccountDetailsSuccessfully() throws Exception {
    //        // Arrange
    //        final NetId testUser = new NetId("SomeUser");
    //        final Password testPassword = new Password("password123");
    //        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
    //        final Gender gender = Gender.MALE;
    //        final String organization = "teamAlpha";
    //        List<TypesOfPositions> positions = new ArrayList<>();
    //        positions.add(TypesOfPositions.COX);
    //        positions.add(TypesOfPositions.COACH);
    //        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
    //        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T15:00");
    //        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-31T20:59");
    //        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-31T22:00");
    //        Availability expectedAvailabilityOne = new Availability(testUser, dateOneIntervalOne, dateTwoIntervalTwo);
    //        Availability expectedAvailabilityTwo = new Availability(testUser, dateOneIntervalTwo, dateTwoIntervalTwo);
    //        UserCertificate expectedUserCertificateOne = new UserCertificate(testUser, "C4");
    //        UserCertificate expectedUserCertificateTwo = new UserCertificate(testUser, "8+");
    //        TreeMap<LocalDateTime, LocalDateTime> availabilities = new TreeMap<>();
    //        availabilities.put(dateOneIntervalOne, dateTwoIntervalOne);
    //        availabilities.put(dateOneIntervalTwo, dateTwoIntervalTwo);
    //        List<String> certificates = List.of("C4", "8+");
    //        List<UserCertificate> userCertificates = List.of(expectedUserCertificateOne, expectedUserCertificateTwo);
    //        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
    //        // Act
    //        accountDetailsService.setAccountDetails(testUser,
    //                                                testPassword,
    //                                                gender,
    //                                                positions,
    //                                                availabilities,
    //                                                certificates,
    //                                                organization);
    //
    //        // Assert
    //        User savedUser = userRepository.findByNetId(testUser).orElseThrow();
    //        List<Availability> foundAvailabilities = availabilityRepository.findAllByNetId(testUser);
    //        List<UserCertificate> foundCertificates = userCertificatesRepository.findAllByNetId(testUser);
    //
    //        assertThat(savedUser.getNetId()).isEqualTo(testUser);
    //        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
    //        assertThat(savedUser.getGender().getGender()).isEqualTo(gender.getGender());
    //        assertThat(savedUser.getOrganization()).isEqualTo("teamAlpha");
    //        //assertThat(foundAvailabilities)
    //        //.containsExactlyInAnyOrder(expectedAvailabilityOne, expectedAvailabilityTwo);
    //        //assertThat(foundCertificates)
    //        //.containsExactlyInAnyOrder(expectedUserCertificateOne, expectedUserCertificateTwo);
    //    }

    @Test
    public void setAccountDetails_throwsAvailabilityException() {
        // Arrange
        final NetId testUser = new NetId("NewUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Password newTestPassword = new Password("password456");
        final Gender gender = Gender.MALE;
        List<TypesOfPositions> positions = new ArrayList<>();
        positions.add(TypesOfPositions.COX);
        positions.add(TypesOfPositions.COACH);
        final String organization = "teamAlpha";
        AmateurUser existingAppUser = new AmateurUser(testUser, existingTestPassword, gender, organization);
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T13:00");
        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-12T20:59");
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-12T22:00");
        TreeMap<LocalDateTime, LocalDateTime> availabilities = new TreeMap<>();
        availabilities.put(dateOneIntervalOne, dateTwoIntervalOne);
        availabilities.put(dateOneIntervalTwo, dateTwoIntervalTwo);
        List<String> certificates = List.of("C4", "8+");
        userRepository.save(existingAppUser);

        // Act
        ThrowableAssert.ThrowingCallable action = () -> accountDetailsService.setAccountDetails(testUser,
                newTestPassword,
                gender,
                positions,
                availabilities,
                certificates,
                organization);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);

    }

    //    @Test
    //    public void getUserDetails_worksCorrectly() throws Exception {
    //        NetId netId = new NetId("NetId");
    //        String organisation = "Team Blue";
    //        List<TypesOfPositions> positions = List.of(TypesOfPositions.COX, TypesOfPositions.STARBOARD_ROWER);
    //        List<String> certificates = List.of("C4", "4+");
    //
    //        when(mockPasswordEncoder.hash(any())).thenReturn(new HashedPassword("123"));
    //
    //        accountDetailsService.setAccountDetails(netId,
    //                new Password("123"),
    //                Gender.FEMALE,
    //                positions,
    //                availabilities,
    //                certificates,
    //                organisation);
    //
    //        UserDetailsModel result = accountDetailsService.getAccountDetails(netId);
    //        UserDetailsModel expected = new UserDetailsModel(netId.toString(),
    //                "FEMALE",
    //                organisation,
    //                false,
    //                positions,
    //                availabilitiesList,
    //                certificates);
    //
    //        assertThat(result.getCertificates()).hasSameElementsAs(expected.getCertificates());
    //        assertThat(result.getAvailabilities()).isEqualTo(expected.getAvailabilities());
    //        assertThat(result.getGender()).isEqualTo(expected.getGender());
    //        assertThat(result.getOrganisation()).isEqualTo(expected.getOrganisation());
    //        assertThat(expected.getPositions()).hasSameElementsAs(result.getPositions());
    //    }

    @Test
    public void getUsersDetails_throesException() {
        // Act
        ThrowableAssert.ThrowingCallable action = () -> accountDetailsService.getAccountDetails(new NetId("mihaita"));

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);
    }

    @Test
    public void getUser_works_correctly() throws Exception {
        final NetId testUser = new NetId("NewUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Password newTestPassword = new Password("password456");
        final Gender gender = Gender.MALE;
        List<TypesOfPositions> positions = new ArrayList<>();
        positions.add(TypesOfPositions.COX);
        positions.add(TypesOfPositions.COACH);
        final String organization = "teamAlpha";
        AmateurUser existingAppUser = new AmateurUser(testUser, existingTestPassword, gender, organization);
        when(mockUserRepository.findByNetId(testUser)).thenReturn(Optional.of(existingAppUser));
        assertThat(accountDetailsService.getUser(testUser)).isEqualTo(existingAppUser);
    }
}