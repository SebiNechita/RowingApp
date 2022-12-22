package nl.tudelft.sem.template.rowinginfo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.template.rowinginfo.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.rowinginfo.domain.Certificates;
import nl.tudelft.sem.template.rowinginfo.integration.utils.JsonUtil;
import nl.tudelft.sem.template.rowinginfo.models.CertificatesRequestModel;
import nl.tudelft.sem.template.rowinginfo.repositories.CertificatesRepository;
import nl.tudelft.sem.template.rowinginfo.services.CertificatesService;
import org.junit.jupiter.api.BeforeEach;
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
@ActiveProfiles({"test", "mockTokenVerifier"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CertificatesTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private transient CertificatesService certificatesService;
    @Autowired
    private transient CertificatesRepository certificatesRepository;
    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    private CertificatesRequestModel requestModel;

    private String certificateName;
    private int certificateValue;
    private String description;

    @BeforeEach
    void setup() {
        // Arrange
        this.certificateName = "C4";
        this.certificateValue = 4;
        this.description = "Boats with this certificate can be used in the C4 category";

        this.requestModel = new CertificatesRequestModel(certificateName, certificateValue, description);
    }

    @Test
    public void createCertificate_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        resultActions.andExpect(status().isOk());

        // Assert
        Certificates certificate = certificatesRepository.findById(1).orElseThrow();

        assertThat(certificate.getDescription()).isEqualTo(description);
        assertThat(certificate.getCertificateName()).isEqualTo(certificateName);
        assertThat(certificate.getCertificateValue()).isEqualTo(certificateValue);
    }

    @Test
    public void checkIfCertificateExists_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));

        ResultActions resultActions2 = mockMvc.perform(get("/check/certificates/" + certificateName)
                .header("Authorization","Bearer MockedToken"));

        // Assert
        resultActions.andExpect(status().isOk());

        // Assert
        assertThat(resultActions2.andReturn().getResponse().getContentAsString().contains("true")).isEqualTo(true);
    }

    @Test
    public void checkIfCertificateExists_withWrongData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/check/certificates/" + certificateName)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        resultActions.andExpect(status().isOk());

        // Assert
        assertThat(resultActions.andReturn().getResponse().getContentAsString().contains("true")).isEqualTo(false);
    }

    @Test
    public void checkIfCertificateIsUnique_withWrongData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions resultActions2 = mockMvc.perform(post("/create/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        resultActions.andExpect(status().isOk());
        resultActions2.andExpect(status().isBadRequest());

    }
}
