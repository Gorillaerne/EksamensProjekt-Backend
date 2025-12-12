package gustavo.com.eksamenprojektbackend.User.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserAlreadyExistsException;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;

    @Test
    void testCreateUser_Success() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setUsername("newUser");
        dto.setPassword("secret");
        dto.setEmail("newuser@example.com");
        dto.setRole("USER");

        // Act
        User created = userService.createUser(dto);

        // Assert
        assertNotNull(created.getId());
        assertEquals("newUser", created.getUsername());
        assertEquals("newuser@example.com", created.getEmail());

        // Password skal være encoded → må IKKE matche plain text
        assertNotEquals("secret", created.getPassword());

        // Bekræft at brugeren findes i DB
        assertTrue(userRepository.findByUsername("newUser").isPresent());
    }

    @Test
    void testCreateUser_AlreadyExists() {

        UserDTO dto = new UserDTO();
        dto.setUsername("admin"); // findes i data.sql
        dto.setPassword("1234");
        dto.setEmail("admin@example.com");
        dto.setRole("ADMIN");

        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(dto)
        );

        assertEquals("admin", ex.getMessage());
    }

}
