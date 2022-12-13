package nl.tudelft.sem.template.user.domain;

import nl.tudelft.sem.template.user.domain.userlogic.*;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserAvailabilityRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserCertificatesRepository;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import nl.tudelft.sem.template.user.domain.userlogic.services.AccountDetailsService;
import nl.tudelft.sem.template.user.domain.userlogic.services.PasswordHashingService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
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

    @Test
    void setAccountDetailsSuccessfully() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        final String gender = "male";
        LocalDateTime dateOneIntervalOne = LocalDateTime.parse("2022-12-12T13:30");
        LocalDateTime dateTwoIntervalOne = LocalDateTime.parse("2022-12-12T15:00");
        LocalDateTime dateOneIntervalTwo = LocalDateTime.parse("2022-12-31T20:59");
        LocalDateTime dateTwoIntervalTwo = LocalDateTime.parse("2022-12-31T22:00");
        Availability expectedAvailabilityOne = new Availability(testUser, dateOneIntervalOne, dateTwoIntervalTwo);
        Availability expectedAvailabilityTwo = new Availability(testUser, dateOneIntervalTwo, dateTwoIntervalTwo);
        UserCertificate expectedUserCertificateOne = new UserCertificate(testUser, "C4");
        UserCertificate expectedUserCertificateTwo = new UserCertificate(testUser, "8+");
        TreeMap<LocalDateTime, LocalDateTime> availabilities = new TreeMap<>();
        availabilities.put(dateOneIntervalOne, dateTwoIntervalOne);
        availabilities.put(dateOneIntervalTwo, dateTwoIntervalTwo);
        List<String> certificates = List.of("C4", "8+");
        List<UserCertificate> userCertificates = List.of(expectedUserCertificateOne, expectedUserCertificateTwo);
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
        // Act
        accountDetailsService.setAccountDetails(testUser, testPassword, gender, availabilities, certificates);

        // Assert
        AppUser savedUser = userRepository.findByNetId(testUser).orElseThrow();
        List<Availability> foundAvailabilities = availabilityRepository.findAllByNetId(testUser);
        List<UserCertificate> foundCertificates = userCertificatesRepository.findALlByNetId(testUser);

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getGender()).isEqualTo(gender);
        //assertThat(foundAvailabilities).containsExactlyInAnyOrder(expectedAvailabilityOne, expectedAvailabilityTwo);
        //assertThat(foundCertificates).containsExactlyInAnyOrder(expectedUserCertificateOne, expectedUserCertificateTwo);
    }

    @Test
    public void setAccountDetails_throwsAvailabilityException() {
        // Arrange
        final NetId testUser = new NetId("NewUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Password newTestPassword = new Password("password456");
        final String gender = "male";
        AppUser existingAppUser = new AppUser(testUser, existingTestPassword, gender);
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
                availabilities,
                certificates);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);

        AppUser savedUser = userRepository.findByNetId(testUser).orElseThrow();

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword);
    }
}