package nl.tudelft.sem.template.user.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.user.domain.userlogic.Gender;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import nl.tudelft.sem.template.user.domain.userlogic.entities.AmateurUser;
import nl.tudelft.sem.template.user.domain.userlogic.entities.User;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import nl.tudelft.sem.template.user.domain.userlogic.services.PasswordHashingService;
import nl.tudelft.sem.template.user.integration.util.JsonUtil;
import nl.tudelft.sem.template.user.models.AmateurSetAccountDetailsModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    //@Autowired
    //private transient UserRepository userRepository;

    @Autowired
    private transient UserRepository mockUserRepository;


    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

//    @Test
//    public void setAccountDetails_WorksCorrectly() throws Exception {
//        final NetId testUser = new NetId("SomeUser");
//        final Password testPassword = new Password("password123");
//        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
//        final String testGender = "MALE";
//        final String organization = "teamAlpha";
//        List<TypesOfPositions> positions = new ArrayList<>();
//        positions.add(TypesOfPositions.COX);
//        positions.add(TypesOfPositions.COACH);
//        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
//        AmateurSetAccountDetailsModel model = new AmateurSetAccountDetailsModel();
//        model.setNetId(testUser.toString());
//        model.setPassword(testPassword.toString());
//        model.setGender(testGender);
//        model.setOrganization(organization);
//        model.setPositions(positions);
//        Tuple<String, String> tupleOne = new Tuple<>("2022-12-12T13:30", "2022-12-12T15:00");
//        Tuple<String, String> tupleTwo = new Tuple<>("2022-12-31T20:59", "2022-12-31T22:00");
//        List<Tuple<String, String>> availabilities = List.of(tupleOne, tupleTwo);
//        model.setAvailabilities(availabilities);
//        List<String> certificates = List.of("C4", "8+");
//        model.setCertificates(certificates);
//
//        // Act
//        when(mockAuthenticationManager.getNetId()).thenReturn("SomeUser");
//        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
//        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");
//
//        // Add mock behavior for user repository
//        User mockUser = new AmateurUser();
//        mockUser.setNetId(testUser);
//        mockUser.setPassword(testHashedPassword);
//        mockUser.setGender(Gender.MALE);
//        mockUser.setOrganization("teamAlpha");
//        when(mockUserRepository.findByNetId(testUser)).thenReturn(Optional.of(mockUser));
//        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);
//
//        // Act
//        // Still include Bearer token as AuthFilter itself is not mocked
//        ResultActions result = mockMvc.perform(post("http://localhost:8083/amateur/set/account/details")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer MockedToken")
//                .content(JsonUtil.serialize(model)));
//
//        // Assert
//        result.andExpect(status().isOk());
//
//        // Verify that the save method on the mock repository was called
//        verify(mockUserRepository, times(1)).save(any(User.class));
//
//        // Since we are returning the mock user from the mock repository, we can just use the mock directly
//        // instead of calling findByNetId
//        assertThat(mockUser.getNetId()).isEqualTo(testUser);
//        assertThat(mockUser.getPassword()).isEqualTo(testHashedPassword);
//        assertThat(mockUser.getGender().getGender()).isEqualTo(testGender);
//        assertThat(mockUser.getOrganization()).isEqualTo("teamAlpha");
//    }

    /*
    @Test
    public void setAccountDetails_WorksCorrectly() throws Exception {
        final NetId testUser = new NetId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        final String testGender = "MALE";
        final String organization = "teamAlpha";
        List<TypesOfPositions> positions = new ArrayList<>();
        positions.add(TypesOfPositions.COX);
        positions.add(TypesOfPositions.COACH);
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
        AmateurSetAccountDetailsModel model = new AmateurSetAccountDetailsModel();
        model.setNetId(testUser.toString());
        model.setPassword(testPassword.toString());
        model.setGender(testGender);
        model.setOrganization(organization);
        model.setPositions(positions);
        Tuple<String, String> tupleOne = new Tuple<>("2022-12-12T13:30", "2022-12-12T15:00");
        Tuple<String, String> tupleTwo = new Tuple<>("2022-12-31T20:59", "2022-12-31T22:00");
        List<Tuple<String, String>> availabilities = List.of(tupleOne, tupleTwo);
        model.setAvailabilities(availabilities);
        List<String> certificates = List.of("C4", "8+");
        model.setCertificates(certificates);

        // Act
        when(mockAuthenticationManager.getNetId()).thenReturn("SomeUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        ResultActions result = mockMvc.perform(post("http://localhost:8083/amateur/set/account/details")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(JsonUtil.serialize(model)));

        // Assert
        result.andExpect(status().isOk());

        User savedUser = userRepository.findByNetId(testUser).orElseThrow();

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getGender().getGender()).isEqualTo(testGender);
    }
     */

    /*
    @Test
    public void getAccountDetails_WorksCorrectly() throws Exception {
        final NetId testUser = new NetId("SomeUser");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        final Gender testGender = Gender.MALE;
        final String organization = "teamAlpha";
        List<TypesOfPositions> positions = new ArrayList<>();
        positions.add(TypesOfPositions.COX);
        positions.add(TypesOfPositions.COACH);
        User existingAppUser = new AmateurUser(testUser, testHashedPassword, testGender, organization);
        when(mockUserRepository.findByNetId(new NetId("SomeUser"))).thenReturn(Optional.of(existingAppUser));
        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        ResultActions result = mockMvc.perform(get("http://localhost:8082/user/get/details/SomeUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isOk()).andExpect(content().json(JsonUtil.serialize(UserDetailsModel.class)));
        UserDetailsModel actualModel = (UserDetailsModel) result.andReturn().getModelAndView().getModel();

        assertThat(actualModel.getNetId()).isEqualTo("SomeUser");
        //assertThat(savedUser.getGender().getGender()).isEqualTo(testGender);
    }

     */
}