package ma.cirestechnologies.miniprojet.service;

import ma.cirestechnologies.miniprojet.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
