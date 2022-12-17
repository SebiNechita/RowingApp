package nl.tudelft.sem.template.authentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    /**
     * Logger bean for the authentication microservice.
     * Used in the AuthenticationController to log requests.
     */
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("authentication-microservice");
    }
}
