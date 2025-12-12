package gustavo.com.eksamenprojektbackend.User.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.User.DTO.LoginRequestDTO;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import gustavo.com.eksamenprojektbackend.User.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    @MockBean
    private AuthService authService; // <- Mock, så vi ikke laver JWT i testen

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ILogRepository logRepository;

    @BeforeEach
    void setup() {
        logRepository.deleteAll(); // SLET logs først
        userRepository.deleteAll(); // Nu må du slette users
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_returns201() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("secret");
        dto.setEmail("john@example.com");
        dto.setRole("USER");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_returns200() throws Exception {
        User u = new User();
        u.setUsername("toDelete");
        u.setPassword("pw");
        u.setEmail("d@test.com");
        u.setRole("USER");
        u = userRepository.save(u);

        mockMvc.perform(delete("/api/users/" + u.getId()))
                .andExpect(status().isOk());
    }
}