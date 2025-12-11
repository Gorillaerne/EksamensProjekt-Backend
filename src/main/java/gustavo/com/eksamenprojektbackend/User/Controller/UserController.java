package gustavo.com.eksamenprojektbackend.User.Controller;

import gustavo.com.eksamenprojektbackend.User.DTO.UserRoleDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.DTO.LoginRequestDTO;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Service.AuthService;
import gustavo.com.eksamenprojektbackend.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userservice;
    private final AuthService authService;


    public UserController(UserService userservice, AuthService authService) {
        this.userservice = userservice;
        this.authService = authService;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(Authentication authentication, @PathVariable int id){
        User user = (User) authentication.getPrincipal();
        userservice.deleteUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userservice.getAll(), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest){

            return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getActiveUser(Authentication authentication){
            return ResponseEntity.status(HttpStatus.OK).body(userservice.getActiveUser((User) authentication.getPrincipal()));
    }


    @PostMapping()
    public ResponseEntity<?> createUser (Authentication authentication, @RequestBody UserDTO dto) {
        User user = (User) authentication.getPrincipal();
        User newUser = userservice.createUser(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
