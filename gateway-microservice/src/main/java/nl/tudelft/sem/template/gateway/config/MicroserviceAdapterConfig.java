package nl.tudelft.sem.template.gateway.config;

import nl.tudelft.sem.template.common.communication.ActivityMatchMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.AuthenticationMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.MicroServiceAddresses;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceAdapterConfig {
    @Bean
    UserMicroserviceAdapter userMicroserviceAdapter() {
        return new UserMicroserviceAdapter(MicroServiceAddresses.userMicroservice);
    }

    @Bean
    ActivityMatchMicroserviceAdapter activityMatchMicroserviceAdapter() {
        return new ActivityMatchMicroserviceAdapter(MicroServiceAddresses.activityMatchMicroservice);
    }

    @Bean
    AuthenticationMicroserviceAdapter authenticationMicroserviceAdapter() {
        return new AuthenticationMicroserviceAdapter(MicroServiceAddresses.authenticationMicroservice);
    }
}
