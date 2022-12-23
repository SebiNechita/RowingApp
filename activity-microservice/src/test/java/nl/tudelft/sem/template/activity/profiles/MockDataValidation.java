package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.activity.services.DataValidation;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockDataValidation")
@Configuration
public class MockDataValidation {

    /**
     * Mocks the DataValidation.
     *
     * @return A mocked DataValidation.
     */
    @Bean
    @Primary
    public DataValidation getMockDataValidation() {
        return Mockito.mock(DataValidation.class);
    }
}
