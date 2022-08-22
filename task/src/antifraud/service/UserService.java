package antifraud.service;

import antifraud.model.UserRole;
import antifraud.model.user.User;
import antifraud.repository.UserRepository;
import antifraud.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User signup(SignupRequest signupRequest) {
        boolean isUnlocked = false;
        UserRole role = UserRole.MERCHANT;
        if (userRepository.findAll().isEmpty()) {
            role = UserRole.ADMINISTRATOR;
            isUnlocked = true;
        }
        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }


        User user = new User(
                signupRequest.getName(),
                signupRequest.getUsername(),
                signupRequest.getPassword(),
                role,
                isUnlocked
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            User userToDelete = userRepository.findByUsername(username);
            userRepository.delete(userToDelete);
        }
    }

    public void changeRole(String username, String role) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userRepository.findUserByUsername(username).get();

        if (user.getRole().toString().equals("ROLE_" + role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already assigned");
        } else {
            switch (role) {
                case ("SUPPORT"):
                    user.setRole(UserRole.SUPPORT);
                    userRepository.save(user);
                    break;
                case ("MERCHANT"):
                    user.setRole(UserRole.MERCHANT);
                    userRepository.save(user);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found");
            }

        }
    }

    public void lockUnlock(String username, String operation) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userRepository.findUserByUsername(username).get();
        if (user.isAccountNonLocked() && operation.equals("UNLOCK") || !user.isAccountNonLocked() && operation.equals("LOCK")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        } else if (user.getRole().equals(UserRole.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        } else {
            switch (operation) {
                case ("LOCK"):
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    break;
                case ("UNLOCK"):
                    user.setAccountNonLocked(true);
                    userRepository.save(user);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
            }

        }
    }
}