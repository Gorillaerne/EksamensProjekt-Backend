package gustavo.com.eksamenprojektbackend.User.Service;

import gustavo.com.eksamenprojektbackend.Security.JwtUtil;
import gustavo.com.eksamenprojektbackend.User.DTO.LoginRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDTO("testuser", "password123");
    }

    @Test
    void login_Success_ReturnsTokenAndUsername() {
        // Arrange
        String expectedToken = "jwt.token.here";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("testuser")).thenReturn(expectedToken);

        // Act
        Map<String, Object> result = authService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedToken, result.get("token"));
        assertEquals("testuser", result.get("username"));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken("testuser");
    }

    @Test
    void login_ThrowsExceptionWhenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void login_CreatesCorrectAuthenticationToken() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        // Act
        authService.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(argThat(token ->
                token instanceof UsernamePasswordAuthenticationToken &&
                token.getPrincipal().equals("testuser") &&
                token.getCredentials().equals("password123")
        ));
    }

    @Test
    void login_GeneratesTokenWithCorrectUsername() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("testuser")).thenReturn("generated.token");

        // Act
        Map<String, Object> result = authService.login(loginRequest);

        // Assert
        verify(jwtUtil).generateToken("testuser");
        assertNotNull(result.get("token"));
    }

    @Test
    void login_HandlesEmptyUsername() {
        // Arrange
        LoginRequestDTO emptyUsernameRequest = new LoginRequestDTO("", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(emptyUsernameRequest));
    }

    @Test
    void login_HandlesEmptyPassword() {
        // Arrange
        LoginRequestDTO emptyPasswordRequest = new LoginRequestDTO("testuser", "");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(emptyPasswordRequest));
    }

    @Test
    void login_ReturnsMapWithCorrectKeys() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        // Act
        Map<String, Object> result = authService.login(loginRequest);

        // Assert
        assertTrue(result.containsKey("token"));
        assertTrue(result.containsKey("username"));
    }

    @Test
    void login_DifferentUsernamesGenerateDifferentResponses() {
        // Arrange
        LoginRequestDTO user1Request = new LoginRequestDTO("user1", "pass1");
        LoginRequestDTO user2Request = new LoginRequestDTO("user2", "pass2");
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("user1")).thenReturn("token1");
        when(jwtUtil.generateToken("user2")).thenReturn("token2");

        // Act
        Map<String, Object> result1 = authService.login(user1Request);
        Map<String, Object> result2 = authService.login(user2Request);

        // Assert
        assertEquals("user1", result1.get("username"));
        assertEquals("user2", result2.get("username"));
        assertNotEquals(result1.get("username"), result2.get("username"));
    }
}