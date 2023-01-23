package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.activity.services.ActivityClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockActivityClient")
@Configuration
public class MockActivityClient {
    /**
     * Mocks the ActivityClient.
     *
     * @return A mocked ActivityClient.
     */
    @Bean
    @Primary
    public ActivityClient getMockActivityClient() {
        return Mockito.mock(ActivityClient.class);
    }
}
