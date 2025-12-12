package gustavo.com.eksamenprojektbackend.User.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserAlreadyExistsException;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(IUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_success() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("password123");
        dto.setEmail("john@example.com");
        dto.setRole("USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("ENCODED_PASS");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("john");
        savedUser.setPassword("ENCODED_PASS");
        savedUser.setEmail("john@example.com");
        savedUser.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(dto);

        // Assert
        assertEquals("john", result.getUsername());
        assertEquals("ENCODED_PASS", result.getPassword());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());

        // Verify password encoding was called
        verify(passwordEncoder).encode("password123");

        // Verify save() received the correct user object
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User captured = userCaptor.getValue();
        assertEquals("john", captured.getUsername());
        assertEquals("ENCODED_PASS", captured.getPassword());
    }

    @Test
    void createUser_usernameAlreadyExists_throwsException() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("password123");
        dto.setEmail("john@example.com");
        dto.setRole("USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        // Act + Assert
        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(dto)
        );

        assertEquals("john", ex.getMessage());

        // save() and password encode must never be called
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

}
