package nl.tudelft.sem.template.activity.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import nl.tudelft.sem.template.activity.domain.exceptions.EmptyStringException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockDataValidation"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ValidateDataTest {

    @Autowired
    private transient DataValidation mockDataValidation;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String boatCertificate;

    @BeforeEach
    void setup() throws Exception {
        // Arrange
        this.startTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(10, 0, 0));
        this.endTime = LocalDateTime.of(LocalDate.of(2022, 1, 8),
                LocalTime.of(12, 0, 0));

        this.boatCertificate = "C4";

        when(mockDataValidation.validateOrganisation(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateCertificate(anyString(), anyString())).thenReturn(true);
        when(mockDataValidation.validateData(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        when(mockDataValidation.validateNameAndDescription(any(), any())).thenCallRealMethod();
    }

    @Test
    public void emptyDescription_throwsException() throws Exception {
        // Act
        ThrowableAssert.ThrowingCallable action = () -> mockDataValidation
                .validateNameAndDescription("name", "");

        // Assert
        assertThatExceptionOfType(EmptyStringException.class)
                .isThrownBy(action);
    }

    //Todo: Add more tests. Also add integration tests with rowing info.
}
