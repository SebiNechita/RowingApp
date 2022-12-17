package nl.tudelft.sem.template.user.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.user.domain.userlogic.AppUser;
import nl.tudelft.sem.template.user.domain.userlogic.HashedPassword;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import nl.tudelft.sem.template.user.domain.userlogic.Password;
import nl.tudelft.sem.template.user.domain.userlogic.Tuple;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import nl.tudelft.sem.template.user.domain.userlogic.services.PasswordHashingService;
import nl.tudelft.sem.template.user.integration.util.JsonUtil;
import nl.tudelft.sem.template.user.models.SetAccountDetailsModel;
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
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Test
    public void setAccountDetails_WorksCorrectly() throws Exception {
        final NetId testUser = new NetId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        final String testGender = "male";
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
        SetAccountDetailsModel model = new SetAccountDetailsModel();
        model.setNetId(testUser.toString());
        model.setPassword(testPassword.toString());
        model.setGender(testGender);
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
        ResultActions result = mockMvc.perform(post("/setAccountDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(JsonUtil.serialize(model)));

        // Assert
        result.andExpect(status().isOk());

        AppUser savedUser = userRepository.findByNetId(testUser).orElseThrow();

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getGender()).isEqualTo(testGender);
    }
}