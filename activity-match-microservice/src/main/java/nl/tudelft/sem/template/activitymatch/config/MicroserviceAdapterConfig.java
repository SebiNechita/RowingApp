package nl.tudelft.sem.template.activitymatch.config;

import nl.tudelft.sem.template.common.communication.ActivityOfferMicroserviceAdapter;
import nl.tudelft.sem.template.common.communication.MicroServiceAddresses;
import nl.tudelft.sem.template.common.communication.UserMicroserviceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroserviceAdapterConfig {
    @Bean
    ActivityOfferMicroserviceAdapter activityOfferMicroserviceAdapter() {
        return new ActivityOfferMicroserviceAdapter(MicroServiceAddresses.activityOfferMicroservice);
    }
}
