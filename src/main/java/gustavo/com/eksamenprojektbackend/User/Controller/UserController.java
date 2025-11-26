package gustavo.com.eksamenprojektbackend.User.Controller;

import gustavo.com.eksamenprojektbackend.Models.User;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @GetMapping("")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userservice.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser (@RequestBody UserDTO dto) {
        User newUser = userservice.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
