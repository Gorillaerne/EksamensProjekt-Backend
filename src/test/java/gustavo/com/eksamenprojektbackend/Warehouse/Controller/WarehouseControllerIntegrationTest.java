package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WarehouseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private Authentication authentication;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Gør at Jackson ikke fejler på manglende relationer i Warehouse
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    // ------------------------------------------------------------
    //                 CREATE WAREHOUSE TEST
    // ------------------------------------------------------------
    @Test
    void createWarehouse() throws Exception {

        WarehouseCreateDTO dto = new WarehouseCreateDTO(
                "New Warehouse",
                "Some Address", "some stuff"
        );

        User user = new User();
        user.setUsername("tester");
        when(authentication.getPrincipal()).thenReturn(user);

        Warehouse saved = new Warehouse();
        saved.setId(1);
        saved.setName("New Warehouse");
        saved.setAddress("Some Address");

        when(warehouseService.createWarehouse(any(WarehouseCreateDTO.class), eq(user)))
                .thenReturn(saved);

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


    // ------------------------------------------------------------
    //                 UPDATE WAREHOUSE TEST
    // ------------------------------------------------------------
    @Test
    void updateWarehouse_success() throws Exception {

        User user = new User();
        user.setUsername("tester");
        when(authentication.getPrincipal()).thenReturn(user);

        Warehouse updated = new Warehouse();
        updated.setId(1);
        updated.setName("Updated Warehouse");

        when(warehouseService.updateWarehouse(eq(1), any(Warehouse.class), eq(user)))
                .thenReturn(updated);

        String json = """
        {
          "name": "Updated Warehouse"
        }
        """;

        mockMvc.perform(
                        put("/api/warehouses/1")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Warehouse"));
    }

}
