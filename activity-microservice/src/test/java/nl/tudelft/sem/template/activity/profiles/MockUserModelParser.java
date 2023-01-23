package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.activity.services.DataValidation;
import nl.tudelft.sem.template.activity.services.UserModelParser;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockUserModelParser")
@Configuration
public class MockUserModelParser {

    /**
     * Mocks the UserModelParser.
     *
     * @return A mocked UserModelParser.
     */
    @Bean
    @Primary
    public UserModelParser getMockUserModelParser() {
        return Mockito.mock(UserModelParser.class);
    }
}
