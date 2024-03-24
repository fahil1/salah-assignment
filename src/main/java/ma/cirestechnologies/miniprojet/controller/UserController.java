package ma.cirestechnologies.miniprojet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ma.cirestechnologies.miniprojet.dto.BatchResponse;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.exception.UserBatchGenerationException;
import ma.cirestechnologies.miniprojet.service.AuthService;
import ma.cirestechnologies.miniprojet.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("generate")
    public ResponseEntity<String> generateUsers(@RequestParam(name = "count") Integer count) throws UserBatchGenerationException {
        log.info("generating {} users..", count);
        String usersJson = userService.generateUsers(count);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json")
                .contentLength(usersJson.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(usersJson);

    }

    @GetMapping("me")
    public User fetchUser(@RequestHeader("JWT_TOKEN") String token) throws Exception {
        final String authenticatedUsername = authService.validateToken(token);

        return userService.fetchUser(authenticatedUsername);
    }
    @GetMapping("{username}")
    public User fetchUser(@RequestHeader("JWT_TOKEN") String token, @PathVariable String username) throws Exception {
        final String authenticatedUsername = authService.validateToken(token);
        final User authenticatedUser = userService.fetchUser(authenticatedUsername);
        log.info("fetching user {} (requested by {})..", username, authenticatedUser.getUsername());
        return userService.fetchUserForAuthenticatedUsername(username, authenticatedUser);
    }

    @PostMapping(value = "batch", consumes = "multipart/form-data")
    public BatchResponse uploadUsers(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("received a file that has {} bytes", multipartFile.getBytes().length);
        final String jsonFile = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
        int[] result = userService.parseAndSaveUsersJSON(jsonFile);
        return new BatchResponse(result[0], result[1]);
    }
}