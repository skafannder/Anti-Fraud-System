package antifraud.service;

import antifraud.DTO.UserRemovedDTO;
import antifraud.DTO.UserDataDTO;
import antifraud.DTO.UserLockUnlockDTO;
import antifraud.model.user.UserRole;
import antifraud.model.user.User;
import antifraud.repository.UserRepository;
import antifraud.request.ChangeRoleRequest;
import antifraud.request.LockUnlockRequest;
import antifraud.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public ResponseEntity<UserDataDTO> signup(SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        boolean isUnlocked = false;
        UserRole role = UserRole.MERCHANT;

        if (userRepository.isEmpty()) {
            role = UserRole.ADMINISTRATOR;
            isUnlocked = true;
        }

        User user = new User(signupRequest, role, isUnlocked);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        UserDataDTO userDataDTO = UserDataDTO.create(user);
        return new ResponseEntity<>(userDataDTO, HttpStatus.CREATED);
    }


    public ResponseEntity<List<UserDataDTO>> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDataDTO> userDataList = users.stream().map(UserDataDTO::create).toList();
        return new ResponseEntity<>(userDataList, HttpStatus.OK);
    }


    public ResponseEntity<UserRemovedDTO> deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            User userToDelete = userRepository.findByUsername(username);
            userRepository.delete(userToDelete);
        }
        var responseDTO = new UserRemovedDTO(username, "Deleted successfully!");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDataDTO> changeRole(ChangeRoleRequest request) {
        String username = request.getUsername();
        String role = request.getRole();
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
            UserDataDTO userDataDTO = UserDataDTO.create(user);
            return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
        }
    }

    public ResponseEntity<UserLockUnlockDTO> lockUnlock(LockUnlockRequest request) {
        String username = request.getUsername();
        String operation = request.getOperation();
        if (!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userRepository.findUserByUsername(username).get();
        String status = "";
        if (user.isAccountNonLocked() && operation.equals("UNLOCK") || !user.isAccountNonLocked() && operation.equals("LOCK")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        } else if (user.getRole().equals(UserRole.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        } else {
            switch (operation) {
                case ("LOCK"):
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    status = String.format("User %s locked!", username);
                    break;
                case ("UNLOCK"):
                    user.setAccountNonLocked(true);
                    userRepository.save(user);
                    status = String.format("User %s unlocked!", username);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
            }
            UserLockUnlockDTO dto = new UserLockUnlockDTO(status);
            return new ResponseEntity<>(dto, HttpStatus.OK);

        }
    }
}