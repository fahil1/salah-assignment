package ma.cirestechnologies.miniprojet.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.exception.UnauthenticatedUser;
import ma.cirestechnologies.miniprojet.exception.UserNotFound;
import ma.cirestechnologies.miniprojet.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long validityInMilliseconds = 3600000*24; // 24h

    @Autowired
    private UserRepository userRepository;

    public String authenticateUser(String username, String password) throws Exception {

        final User retrievedUser = userRepository.findByUsername(username);
        if (retrievedUser == null) {
            throw new UserNotFound(String.format("The user %s is not known to the app", username));
        }

        if (!BCrypt.checkpw(password, retrievedUser.getPassword())) {
            throw new UnauthenticatedUser(String.format("The password is incorrect for user %s", retrievedUser.getUsername()));
        }

        return generateToken(retrievedUser);
    }

    public String validateToken(String jwtToken) throws Exception {
        String subject = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
        return subject;
    }

    private String generateToken(User user) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}