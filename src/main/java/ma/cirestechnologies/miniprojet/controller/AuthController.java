package ma.cirestechnologies.miniprojet.controller;

import ma.cirestechnologies.miniprojet.dto.AuthResponse;
import ma.cirestechnologies.miniprojet.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("auth")
    public AuthResponse authenticate(@RequestParam("username") String username,
                                     @RequestParam("password") String password) throws Exception {
       final String token = authService.authenticateUser(username, password);
       return new AuthResponse(token);
    }
}
