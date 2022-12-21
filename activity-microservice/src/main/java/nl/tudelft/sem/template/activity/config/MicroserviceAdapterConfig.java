package nl.tudelft.sem.template.activity.config;

import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceAdapterConfig {
    @Bean
    UserMicroserviceAdapter userMicroserviceAdapter() {
        return new UserMicroserviceAdapter();
    }
}
