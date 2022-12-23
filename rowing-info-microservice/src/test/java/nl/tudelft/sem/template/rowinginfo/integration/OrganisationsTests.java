package nl.tudelft.sem.template.rowinginfo.integration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.template.rowinginfo.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import nl.tudelft.sem.template.rowinginfo.integration.utils.JsonUtil;
import nl.tudelft.sem.template.rowinginfo.models.OrganisationsRequestModel;
import nl.tudelft.sem.template.rowinginfo.repositories.OrganisationsRepository;
import nl.tudelft.sem.template.rowinginfo.services.OrganisationsService;
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
public class OrganisationsTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private transient OrganisationsService organisationsService;
    @Autowired
    private transient OrganisationsRepository organisationsRepository;
    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    private OrganisationsRequestModel requestModel;

    private String organisationsName;

    @BeforeEach
    void setup() {
        // Arrange
        this.organisationsName = "Delft University of Technology";
        this.requestModel = new OrganisationsRequestModel(organisationsName);
    }

    @Test
    public void createOrganisations_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/organisations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        resultActions.andExpect(status().isOk());

        Organisations organisations = organisationsRepository.findById(1).orElseThrow();
        assertThat(organisations.getOrganisationsName()).isEqualTo(organisationsName);
    }

    @Test
    public void checkIfOrganisationsExists_withValidData_worksCorrectly() throws Exception {
        // Arrange
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create/organisations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(requestModel))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions resultActions2 = mockMvc.perform(get("/check/organisations/" + organisationsName)
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        resultActions.andExpect(status().isOk());

        // Assert
        assertThat(resultActions2.andReturn().getResponse().getContentAsString().contains("true")).isEqualTo(true);
    }

}
