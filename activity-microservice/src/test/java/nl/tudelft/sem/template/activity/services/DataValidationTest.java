package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import nl.tudelft.sem.template.activity.domain.exceptions.InvalidCertificateException;
import nl.tudelft.sem.template.activity.domain.exceptions.NotCorrectIntervalException;
import nl.tudelft.sem.template.common.http.HttpUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

class DataValidationTest {

    private DataValidation dataValidation;
    private HttpUtils httpUtils;

    @BeforeEach
    void setup() {
        this.dataValidation = Mockito.mock(DataValidation.class);
        this.httpUtils = Mockito.mock(HttpUtils.class);
    }

    @Test
    void validateTime_incorrect() {
        LocalDateTime start = LocalDateTime.of(2022, 12, 24, 10, 00);
        LocalDateTime end = LocalDateTime.of(2022, 12, 24, 9, 00);
        assertThrows(NotCorrectIntervalException.class, () -> {
            dataValidation.validateTime(start, end);
        });
        assertThrows(NotCorrectIntervalException.class, () -> {
            dataValidation.validateTime(start, start);
        });
    }

    @Test
    void validateTime_correct() throws NotCorrectIntervalException {
        LocalDateTime start = LocalDateTime.of(2022, 12, 24, 10, 00);
        LocalDateTime end = LocalDateTime.of(2022, 12, 24, 12, 00);
        assertTrue(dataValidation.validateTime(start, end));
    }

    @Test
    void validateNameAndDescription_correct() throws EmptyStringException {
        String name = "belmondawg";
        String description = "kox trening";
        assertThat(dataValidation.validateNameAndDescription(name, description)).isTrue();
    }

    @Test
    void validateNameAndDescription_incorrect() {
        assertThrows(EmptyStringException.class, () -> {
            dataValidation.validateNameAndDescription("szef", "");
        }, "Description");
        assertThrows(EmptyStringException.class, () -> {
            dataValidation.validateNameAndDescription("", "");
        }, "Name");
        assertThrows(EmptyStringException.class, () -> {
            dataValidation.validateNameAndDescription("", "pressing iron");
        }, "Name");
    }

    // Todo: redo this test

    //    @Test
    //    void validateCertificate_correct() throws Exception {
    //        //        when(dataValidation.validateCertificate(anyString(), anyString())).thenReturn(true);
    //        String certificate = "cert";
    //        String rowingInfoMicroservice = "http://localhost:8082/check/certificates";
    //        // Certificate Existance Model is no longer used :((
    //        CertificatesExistanceRequestModel request = new CertificatesExistanceRequestModel(certificate);
    //        Mockito.when(httpUtils.sendAuthorizedHttpRequest(rowingInfoMicroservice, HttpMethod.GET, anyString(), request,
    //                boolean.class)).thenReturn(ResponseEntity.ok(true));
    //        assertThat(dataValidation.validateCertificate(certificate, "siem")).isTrue();
    //
    //    }

    @Test
    void validateCertificate_incorrect() throws Exception {
        String certificate = "cert";
        String rowingInfoMicroservice = "http://localhost:8082/check/certificates";
        when(dataValidation.validateCertificate(rowingInfoMicroservice, anyString()))
                .thenReturn(ResponseEntity.ok(false).hasBody());
        assertThrows(InvalidCertificateException.class, () -> {
            dataValidation.validateCertificate(certificate, "token");
        });
    }


    @Test
    void validateOrganisation() {
    }
}