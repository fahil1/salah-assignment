package ma.cirestechnologies.miniprojet.controller;

import com.github.javafaker.Faker;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private UserService userService;
    private Faker faker;

    UserController(UserService userService, Faker faker) {
        this.userService = userService;
        this.faker = faker;
    }

    @GetMapping("generate/{count}")
    public List<User> generateUsers(@PathVariable Integer count){
        List<User> users = IntStream.range(0, count)
                .mapToObj(i -> new User(
                        null,
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.date().birthday(),
                        faker.address().cityName(),
                        faker.address().country(),
                        faker.internet().avatar(),
                        faker.company().name(),
                        faker.job().position(),
                        faker.phoneNumber().cellPhone(),
                        faker.name().firstName() + '.' + faker.name().lastName() + faker.number().digits(2),
                        faker.internet().emailAddress(),
                        faker.internet().password(),
                        faker.options().option("USER", "ADMIN", "GUEST")
                )).collect(Collectors.toList());
        return users;
    }
}