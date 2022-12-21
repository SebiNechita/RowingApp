package nl.tudelft.sem.template.gateway.config;

import nl.tudelft.sem.template.common.communication.ActivityMatchMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.AuthenticationMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceAdapterConfig {
    @Bean
    UserMicroserviceAdapter userMicroserviceAdapter() {
        return new UserMicroserviceAdapter();
    }

    @Bean
    ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter() {
        return new ActivityMatchMicroserviceAdapter();
    }

    @Bean
    AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter() {
        return new AuthenticationMicroserviceAdapter();
    }
}
