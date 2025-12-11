package gustavo.com.eksamenprojektbackend.User.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.LoginFailedException;
import gustavo.com.eksamenprojektbackend.Security.JwtUtil;
import gustavo.com.eksamenprojektbackend.User.DTO.LoginRequestDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public Map<String,Object> login(LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            String token = jwtUtil.generateToken(loginRequest.username());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", loginRequest.username());
            return response;

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new LoginFailedException("Login mislykkedes: Forkert brugernavn eller adgangskode", e);
        } catch (Exception e) {
            throw new LoginFailedException("Noget gik galt under login", e);
        }
    }
}
