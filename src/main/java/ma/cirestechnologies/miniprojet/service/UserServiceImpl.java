package ma.cirestechnologies.miniprojet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.exception.UnauthorizedUserException;
import ma.cirestechnologies.miniprojet.exception.UserBatchGenerationException;
import ma.cirestechnologies.miniprojet.exception.UserNotFoundException;
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

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final Faker faker;
    UserServiceImpl(UserRepository userRepository, ObjectMapper objectMapper, Faker faker) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.faker = faker;
    }

    @Override
    public String generateUsers(int count) throws UserBatchGenerationException {
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

        String usersJson;
        try {
            usersJson = objectMapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            throw new UserBatchGenerationException("Cannot generate the JSON of the users", e);
        }
        return usersJson;
    }

    @Override
    public User fetchUser(String username) throws UserNotFoundException {
        log.info("fetching the user {}..", username);
        final User user = userRepository.findByUsername(username);
        if (user == null) {
           throw new UserNotFoundException(String.format("The user %s is not known to the app", username));
        }
        return user;
    }

    @Override
    public User fetchUserForAuthenticatedUsername(String username, User authenticated) throws UnauthorizedUserException, UserNotFoundException {
        if ("ADMIN".equals(authenticated.getRole()) || authenticated.getUsername().equals(username)) {
            return this.fetchUser(username);
        }
        else {
            throw new UnauthorizedUserException(String.format("User %s is not authorized to fetch this user %s", authenticated.getUsername(), username));
        }
    }

    @Override
    public int[] parseAndSaveUsersJSON(String usersJson) throws JsonProcessingException {
        final List<User> allUsers = objectMapper.readValue(usersJson, new TypeReference<>() {});
        final List<User> savedUsers = new ArrayList<>();
        final List<User> unsavedUsers = new ArrayList<>();
        log.info("{} users were parsed successfully, saving them now..", allUsers.size());
        for (User user: allUsers) {
            try {
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
        userRepository.save(user);
        savedUsers.add(user);
    }
}


