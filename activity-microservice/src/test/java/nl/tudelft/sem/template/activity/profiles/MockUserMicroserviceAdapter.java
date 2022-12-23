package nl.tudelft.sem.template.activity.profiles;

import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockUserMicroserviceAdapter")
@Configuration
public class MockUserMicroserviceAdapter {

    @Bean
    @Primary
    public UserMicroserviceAdapter getUserMicroserviceAdapter() {
        return Mockito.mock(UserMicroserviceAdapter.class);
    }
}
