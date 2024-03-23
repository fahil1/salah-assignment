package ma.cirestechnologies.miniprojet;

import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CirestechMiniProjectApplication {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    public static void main(String[] args) {
        SpringApplication.run(CirestechMiniProjectApplication.class, args);
    }

}
