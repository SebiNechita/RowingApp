package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.activity.services.ActivityOfferService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockActivityOfferService")
@Configuration
public class MockActivityOfferServiceProfile {

    @Bean
    @Primary
    public ActivityOfferService getMockActivityOfferService() {
        return Mockito.mock(ActivityOfferService.class);
    }
}
