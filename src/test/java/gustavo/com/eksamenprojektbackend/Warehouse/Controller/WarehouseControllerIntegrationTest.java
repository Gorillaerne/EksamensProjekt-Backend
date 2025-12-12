package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gustavo.com.eksamenprojektbackend.Security.JwtAuthFilter;
import gustavo.com.eksamenprojektbackend.Security.JwtUtil;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseController.class)
@AutoConfigureMockMvc(addFilters = false)
class WarehouseControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private Authentication authentication;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWarehouse() throws Exception {

        WarehouseCreateDTO dto = new WarehouseCreateDTO(
                "New Warehouse",
                "Test Address",
                "Test Description"
        );

        User user = new User();
        user.setUsername("tester");
        when(authentication.getPrincipal()).thenReturn(user);

        Warehouse created = new Warehouse("New Warehouse", "Test Address", "Test Description");
        created.setId(1);

        when(warehouseService.createWarehouse(any(WarehouseCreateDTO.class), eq(user)))
                .thenReturn(created);

        mockMvc.perform(
                        post("/api/warehouses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .principal(authentication)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Warehouse"));
    }

    @Test
    void updateWarehouse() throws Exception {

        WarehouseDTO dto = new WarehouseDTO(
                2,
                "some warehouse",
                "Updated Address",
                "Updated Description"
        );

        User user = new User();
        user.setUsername("tester");
        when(authentication.getPrincipal()).thenReturn(user);

        Warehouse updated = new Warehouse();
        updated.setId(1);
        updated.setName("Updated Name");
        updated.setAddress("Updated Address");
        updated.setDescription("Updated Description");

        when(warehouseService.updateWarehouse(eq(1), any(WarehouseDTO.class), eq(user)))
                .thenReturn(updated);

        mockMvc.perform(
                        patch("/api/warehouses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }
}
