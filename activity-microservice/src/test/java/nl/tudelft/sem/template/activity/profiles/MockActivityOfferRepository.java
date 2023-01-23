package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.activity.repositories.ActivityOfferRepository;
import nl.tudelft.sem.template.activity.services.DataValidation;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockActivityOfferRepository")
@Configuration
public class MockActivityOfferRepository {

    /**
    * Mocks the ActivityOfferRepository.
    *
    * @return A mocked ActivityOfferRepository.
    * */
    @Bean
    @Primary
    public ActivityOfferRepository getMockActivityOfferRepository() {
        return Mockito.mock(ActivityOfferRepository.class);
    }
}
