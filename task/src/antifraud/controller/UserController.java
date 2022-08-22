package antifraud.controller;

import antifraud.model.user.User;
import antifraud.request.ChangeRoleRequest;
import antifraud.request.LockUnlockRequest;
import antifraud.request.SignupRequest;
import antifraud.response.UserDataResponse;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping(value = "/api/auth/user")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        User user = userService.signup(signupRequest);
        UserDataResponse userDataResponse = UserDataResponse.createUserDataResponse(user);
        return new ResponseEntity<>(userDataResponse, HttpStatus.CREATED);
    }


    @GetMapping(value = "/api/auth/list")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.findAll();
        List<UserDataResponse> userDataResponses = new ArrayList<>();

        // Convert User objects to UserDataResponse objects
        for (User u : users) {
            UserDataResponse userDataResponse = UserDataResponse.createUserDataResponse(u);
            userDataResponses.add(userDataResponse);
        }

        return new ResponseEntity<>(userDataResponses, HttpStatus.OK);
    }


    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);

        var response = Map.of(
                "username", username,
                "status", "Deleted successfully!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<?> changeRole(@RequestBody @Valid ChangeRoleRequest request) {
        userService.changeRole(request.getUsername(), request.getRole());

        var response = UserDataResponse.createUserDataResponse(userService.findByUsername(request.getUsername()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<?> lockUnlockUser(@RequestBody @Valid LockUnlockRequest request) {
        userService.lockUnlock(request.getUsername(), request.getOperation());

        var response = Map.of("status", "USER " + request.getUsername() + " " + (request.getOperation().equals("LOCK") ? "locked!" : "unlocked!"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}