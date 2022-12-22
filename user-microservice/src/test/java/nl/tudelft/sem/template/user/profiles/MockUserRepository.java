package nl.tudelft.sem.template.user.profiles;

import nl.tudelft.sem.template.user.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.user.domain.userlogic.repos.UserRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockUserRepository")
@Configuration
public class MockUserRepository {
    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject an AuthenticationManager
    public UserRepository getMockUserRepository() {
        return Mockito.mock(UserRepository.class);
    }
}
