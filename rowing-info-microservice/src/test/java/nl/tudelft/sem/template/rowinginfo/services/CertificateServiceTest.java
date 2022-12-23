package nl.tudelft.sem.template.rowinginfo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.template.rowinginfo.domain.Certificates;
import nl.tudelft.sem.template.rowinginfo.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.rowinginfo.repositories.CertificatesRepository;
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
public class CertificateServiceTest {
    @Autowired
    private transient CertificatesService certificatesService;
    @Autowired
    private transient CertificatesRepository certificatesRepository;

    private String certificateName;
    private int certificateValue;
    private String description;

    @BeforeEach
    void setup() {
        // Arrange
        this.certificateName = "C4";
        this.certificateValue = 4;
        this.description = "Boats with this certificate can be used in the C4 category";
    }

    @Test
    public void createCertificates_withValidData_worksCorrectly() throws Exception {
        // Act
        certificatesService.createCertificate(certificateName, certificateValue, description);

        // Assert
        Certificates certificate = certificatesRepository.findById(1).orElseThrow();


        assertThat(certificate.getDescription()).isEqualTo(description);
        assertThat(certificate.getCertificateName()).isEqualTo(certificateName);
        assertThat(certificate.getCertificateValue()).isEqualTo(certificateValue);
    }

    @Test
    public void createCertificate_withEmptyName_throwsException() {
        // Arrange
        this.certificateName = "";

        // Act
        ThrowableAssert.ThrowingCallable action = () -> certificatesService
                .createCertificate(certificateName, certificateValue, description);

        // Assert
        assertThatExceptionOfType(EmptyStringException.class)
                .isThrownBy(action);
    }

    @Test
    public void createCertificate_withEmptyDescription_throwsException() {
        // Arrange
        this.description = "";

        // Act
        ThrowableAssert.ThrowingCallable action = () -> certificatesService
                .createCertificate(certificateName, certificateValue, description);

        // Assert
        assertThatExceptionOfType(EmptyStringException.class)
                .isThrownBy(action);
    }

    @Test
    public void checkCertificates_withValidData_worksCorrectly() throws Exception {
        // Act
        certificatesService.createCertificate(certificateName, certificateValue, description);

        // Assert
        Certificates certificate = certificatesRepository.findById(1).orElseThrow();


        assertThat(certificatesService.checkCertificates(certificate.getCertificateName())).isEqualTo(true);
    }

    @Test
    public void checkCertificateisUnique_withValidName_throwsException() throws Exception {
        // Act
        certificatesService.createCertificate(certificateName, certificateValue, description);

        // Assert
        Certificates certificate = certificatesRepository.findById(1).orElseThrow();


        assertThat(certificatesService.checkCertificates(certificate.getCertificateName())).isEqualTo(true);
        ThrowableAssert.ThrowingCallable action = () -> certificatesService
                .createCertificate(certificateName, certificateValue, description);
    }

    @Test
    public void deleteExistentCertificate() throws Exception {
        // Act
        certificatesService.createCertificate(certificateName, certificateValue, description);

        //Assert
        Certificates certificate = certificatesRepository.findById(1).orElseThrow();
        certificatesService.deleteCertificate(1);

        assertThat(certificatesService.checkCertificates(certificate.getCertificateName())).isEqualTo(false);
    }

    @Test
    public void deleteNonExistentCertificate_throwsException() throws Exception {
        // Act
        certificatesService.createCertificate(certificateName, certificateValue, description);

        //Assert
        boolean thrown = false;
        try {
            certificatesService.deleteCertificate(2);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void verifyAdminPermissionForNotLoggedInUser() throws Exception {
        assertThat(certificatesService.adminPermission()).isEqualTo(false);
    }
}
