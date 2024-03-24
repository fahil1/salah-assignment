package ma.cirestechnologies.miniprojet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.exception.UnauthorizedUserException;
import ma.cirestechnologies.miniprojet.exception.UserBatchGenerationException;
import ma.cirestechnologies.miniprojet.exception.UserNotFoundException;

public interface UserService {

    String generateUsers(int count) throws UserBatchGenerationException;
    User fetchUser(String username) throws UserNotFoundException;
    User fetchUserForAuthenticatedUsername(String username, User authenticatedUser) throws UnauthorizedUserException, UserNotFoundException;
    int[] parseAndSaveUsersJSON(final String usersJson) throws JsonProcessingException;
}
