package ma.cirestechnologies.miniprojet.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

    int[] parseAndSaveUsersJSON(final String usersJson) throws JsonProcessingException;
}
