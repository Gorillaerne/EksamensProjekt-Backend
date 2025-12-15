package gustavo.com.eksamenprojektbackend.Config;

import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import gustavo.com.eksamenprojektbackend.User.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    private final UserService userService;
    private final IUserRepository userRepository;

    public Init(UserService userService, IUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            userService.createUser(new UserDTO("admin", adminPassword , adminEmail , "ROLE_ADMIN"), null);
        } catch (Exception e) {
            System.out.println("Admin ekstisrere allerede :)");
        }


    }
}
