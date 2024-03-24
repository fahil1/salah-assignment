package ma.cirestechnologies.miniprojet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ma.cirestechnologies.miniprojet.entity.User;

import java.util.List;

public interface UserService {

    List<User> generateUsers(int count);
    User fetchUser(String username);
    int[] parseAndSaveUsersJSON(final String usersJson) throws JsonProcessingException;
}
