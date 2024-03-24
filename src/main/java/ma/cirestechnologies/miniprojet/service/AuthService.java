package ma.cirestechnologies.miniprojet.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ma.cirestechnologies.miniprojet.entity.User;
import ma.cirestechnologies.miniprojet.exception.UnauthenticatedUserException;
import ma.cirestechnologies.miniprojet.exception.UserNotFoundException;
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
    @Value("${jwt.duration}")
    private long validityInSeconds;

    @Autowired
    private UserRepository userRepository;

    public String authenticateUser(String username, String password) throws Exception {

        final User retrievedUser = userRepository.findByUsername(username);
        if (retrievedUser == null) {
            throw new UserNotFoundException(String.format("The user %s is not known to the app", username));
        }

        if (!BCrypt.checkpw(password, retrievedUser.getPassword())) {
            throw new UnauthenticatedUserException(String.format("The password is incorrect for user %s", retrievedUser.getUsername()));
        }

        return generateToken(retrievedUser);
    }

    public String validateToken(String jwtToken) throws UnauthenticatedUserException {
        String subject;
        try {
            subject = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();
            if (StringUtils.isEmpty(subject)) {
                throw new UnauthenticatedUserException("The user cannot be authenticated with " + jwtToken);
            }
        } catch (Exception e) {
           throw new UnauthenticatedUserException(e);
        }
        return subject;
    }

    private String generateToken(User user) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds*1000);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}