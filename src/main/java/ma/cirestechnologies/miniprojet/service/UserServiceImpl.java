package ma.cirestechnologies.miniprojet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ObjectMapper objectMapper;
    private Faker faker;
    UserServiceImpl(UserRepository userRepository, ObjectMapper objectMapper, Faker faker) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.faker = faker;
    }

    @Override
    public List<User> generateUsers(int count) {
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
                )).toList();
        return users;
    }

    @Override
    public User fetchUser(String username) {
        log.info("fetching the user {}..", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public int[] parseAndSaveUsersJSON(String usersJson) throws JsonProcessingException {
        final List<User> allUsers = objectMapper.readValue(usersJson, new TypeReference<>() {});
        final List<User> savedUsers = new ArrayList<>();
        final List<User> unsavedUsers = new ArrayList<>();
        log.info("{} users were parsed successfully, saving them now..", allUsers.size());
        for (User user: allUsers) {
            try {
                userRepository.save(user);
                saveUserAndHashPwd(user, savedUsers);
                log.info("{} saved", user.getUsername());
            } catch (Exception e) {
               log.error("Cannot save the user {} for the reason {}", user, e.getMessage(), e);
               unsavedUsers.add(user);
            }
        }
        return new int[] {savedUsers.size(), unsavedUsers.size()};
    }

    @Transactional
    protected void saveUserAndHashPwd(User user, List<User> savedUsers) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        savedUsers.add(user);
    }
}


