package nl.tudelft.sem.template.rowinginfo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.template.rowinginfo.domain.Organisations;
import nl.tudelft.sem.template.rowinginfo.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.rowinginfo.repositories.OrganisationsRepository;
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
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrganisationsServiceTest {

    @Autowired
    private transient OrganisationsService organisationsService;
    @Autowired
    private transient OrganisationsRepository organisationsRepository;

    private String organisationsName;

    @BeforeEach
    void setup() {
        // Arrange
        this.organisationsName = "Delft University of Technology";
    }

    @Test
    public void createOrganisations_withValidData_worksCorrectly() throws Exception {
        // Act
        organisationsService.createOrganisations(organisationsName);

        // Assert
        Organisations organisations = organisationsRepository.findById(1).orElseThrow();

        assertThat(organisations.getOrganisationsName()).isEqualTo(organisationsName);
    }

    @Test
    public void createOrganisations_withEmptyName_throwsException() {
        // Arrange
        this.organisationsName = "";

        // Act
        ThrowableAssert.ThrowingCallable act = () -> organisationsService.createOrganisations(organisationsName);

        // Assert
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(act);
    }

    @Test
    public void checkOrganisations_withValidData_worksCorrectly() throws Exception {
        // Act
        organisationsService.createOrganisations(organisationsName);

        // Assert
        Organisations organisations = organisationsRepository.findById(1).orElseThrow();

        assertThat(organisationsService.checkOrganisations(organisationsName)).isEqualTo(true);
    }

    @Test
    public void checkOrganisationIsUnique_withValidName_throwsException() throws Exception {
        // Act
        organisationsService.createOrganisations(organisationsName);

        // Assert
        Organisations organisations = organisationsRepository.findById(1).orElseThrow();

        assertThat(organisationsService.checkOrganisations(organisationsName)).isEqualTo(true);
        ThrowableAssert.ThrowingCallable act = () -> organisationsService.createOrganisations(organisationsName);
    }

    @Test
    public void deleteExistentOrganisation() throws Exception {
        // Act
        organisationsService.createOrganisations(organisationsName);

        //Assert
        Organisations organisation = organisationsRepository.findById(1).orElseThrow();
        organisationsService.deleteOrganisation(1);

        assertThat(organisationsService.checkOrganisations(organisation.getOrganisationsName())).isEqualTo(false);
    }

    @Test
    public void deleteNonExistentOrganisation_throwsException() throws Exception {
        // Act
        organisationsService.createOrganisations(organisationsName);

        //Assert
        boolean thrown = false;
        try {
            organisationsService.deleteOrganisation(2);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void verifyAdminPermissionForNotLoggedInUser() throws Exception {
        assertThat(organisationsService.adminPermission()).isEqualTo(false);
    }
}
