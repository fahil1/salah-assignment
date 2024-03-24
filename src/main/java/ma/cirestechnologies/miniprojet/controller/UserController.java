package ma.cirestechnologies.miniprojet.controller;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import ma.cirestechnologies.miniprojet.dto.BatchResponse;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private UserService userService;
    private ObjectMapper objectMapper;

    UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("generate")
    public ResponseEntity<String> generateUsers(@RequestParam(name = "count") Integer count) {
        try {
            log.info("generating {} users..", count);
            List<User> users = userService.generateUsers(count);
            String usersJson = objectMapper.writeValueAsString(users);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json")
                    .contentLength(usersJson.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(usersJson);

        } catch (Exception e) {
            log.error("Failed to generate user JSON file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "batch", consumes = "multipart/form-data")
    public BatchResponse uploadUsers(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("received a file that has {} bytes", multipartFile.getBytes().length);
        final String jsonFile = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
        int[] result = userService.parseAndSaveUsersJSON(jsonFile);
        return new BatchResponse(result[0], result[1]);
    }
}