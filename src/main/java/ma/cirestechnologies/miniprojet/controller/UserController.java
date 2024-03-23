package ma.cirestechnologies.miniprojet.controller;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import ma.cirestechnologies.miniprojet.dto.BatchResponse;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private UserService userService;
    private Faker faker;

    UserController(UserService userService, Faker faker) {
        this.userService = userService;
        this.faker = faker;
    }

    @GetMapping("generate/{count}")
    public List<User> generateUsers(@PathVariable Integer count){
        log.info("generating {} users..", count);
        List<User> users = LongStream.range(0L, count)
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
                        faker.options().option("USER", "ADMIN")
                )).collect(Collectors.toList());
        return users;
    }

    @PostMapping(value = "batch", consumes = "multipart/form-data")
    public BatchResponse uploadUsers(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("received a file that has {} bytes", multipartFile.getBytes().length);
        final String jsonFile = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
        int[] result = userService.parseAndSaveUsersJSON(jsonFile);
        return new BatchResponse(result[0], result[1]);
    }
}