package gustavo.com.eksamenprojektbackend.User.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // <--- FIX: Deaktiver security filtre
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ILogRepository logRepository;

    @Autowired
    ObjectMapper objectMapper;

    private Authentication authAdmin() {
        User admin = new User();
        admin.setUsername("admin123");
        admin.setPassword("pw");
        admin.setEmail("admin@test.com");
        admin.setRole("ADMIN");

        admin = userRepository.save(admin);

        return new UsernamePasswordAuthenticationToken(
                admin,
                null,
                admin.getAuthorities()
        );
    }

    @BeforeEach
    void setup() {
        logRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void createUser() throws Exception {

        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("secret");
        dto.setEmail("john@example.com");
        dto.setRole("USER");

        mockMvc.perform(post("/api/users")
                        .with(request -> {
                            request.setUserPrincipal(authAdmin());
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }


    @Test
    void deleteUser() throws Exception {

        User u = new User();
        u.setUsername("deleteme");
        u.setPassword("pw");
        u.setEmail("del@test.com");
        u.setRole("USER");
        u = userRepository.save(u);

        mockMvc.perform(delete("/api/users/" + u.getId())
                        .with(request -> {
                            request.setUserPrincipal(authAdmin());
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}
