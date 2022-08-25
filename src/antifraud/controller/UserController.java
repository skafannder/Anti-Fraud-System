package antifraud.controller;

import antifraud.DTO.UserRemovedDTO;
import antifraud.DTO.UserLockUnlockDTO;
import antifraud.request.ChangeRoleRequest;
import antifraud.request.LockUnlockRequest;
import antifraud.request.SignupRequest;
import antifraud.DTO.UserDataDTO;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping(value = "/api/auth/user")
    public ResponseEntity<UserDataDTO> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return userService.signup(signupRequest);
    }

    @GetMapping(value = "/api/auth/list")
    public ResponseEntity<List<UserDataDTO>> getUsers() {
       return userService.findAll();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<UserRemovedDTO> deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<UserDataDTO> changeRole(@RequestBody @Valid ChangeRoleRequest request) {
        return userService.changeRole(request);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<UserLockUnlockDTO> lockUnlockUser(@RequestBody @Valid LockUnlockRequest request) {
        return userService.lockUnlock(request);

    }


}