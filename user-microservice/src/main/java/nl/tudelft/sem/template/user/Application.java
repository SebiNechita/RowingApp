package nl.tudelft.sem.template.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application {
    public static void main(String[] args) {
        Application.run(Application.class, args);
    }
}
