package nl.tudelft.sem.template.authentication.config;

import javax.sql.DataSource;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

/**
 * The H2 config.
 */
@Configuration
@EnableJpaRepositories("nl.tudelft.sem.template.authentication.domain")
@PropertySource("classpath:application-dev.properties")
@EnableTransactionManagement
public class H2Config {

    @Getter
    private final Environment environment;

    public H2Config(Environment environment) {
        this.environment = environment;
    }

    /**
     * Set up the connection to the database.
     *
     * @return The data source.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.pass"));

        return dataSource;
    }
}
