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

    @GetMapping("")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userservice.getAll(), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest){
        try {

            return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
        } catch (Exception e) {
            return  ResponseEntity.status(401).body("Forkert kode eller brugernavn");
        }

    }

    @GetMapping("/me")
    public ResponseEntity<?> getActiveUser(Authentication authentication){
            return ResponseEntity.status(HttpStatus.OK).body(userservice.getActiveUser((User) authentication.getPrincipal()));
    }


    @PostMapping("")
    public ResponseEntity<?> createUser (@RequestBody UserDTO dto) {
        User newUser = userservice.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
