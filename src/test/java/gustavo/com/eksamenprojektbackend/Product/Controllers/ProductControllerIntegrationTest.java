package gustavo.com.eksamenprojektbackend.Product.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Service.ProductService;
import gustavo.com.eksamenprojektbackend.Security.JwtUtil;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private Authentication authentication;

    @MockBean
    JwtUtil jwtUtil;
    @MockBean
    UserDetailsService userDetailsService;


    @Test
    void createProduct() throws Exception {

        ProductDTO dto = new ProductDTO(
                0, "Test Product", "Description", "picture.png", "SKU123", 99.99, 0
        );

        User mockUser = new User();
        mockUser.setUsername("john");

        when(authentication.getPrincipal()).thenReturn(mockUser);

        ProductDTO saved = new ProductDTO(
                1, "Test Product", "Description", "picture.png", "SKU123", 99.99, 0
        );

        when(productService.createProduct(any(ProductDTO.class), any(User.class)))
                .thenReturn(saved);

        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .principal(authentication)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }


    @Test
    void updateProduct() throws Exception {

        ProductDTO dto = new ProductDTO(
                0, "Updated", "Updated desc", "updated.png", "SKU555", 199.99, 0
        );

        User mockUser = new User();
        mockUser.setUsername("john");

        when(authentication.getPrincipal()).thenReturn(mockUser);

        Product updated = new Product();
        updated.setId(1);
        updated.setName("Updated");
        updated.setDescription("Updated desc");
        updated.setPicture("updated.png");
        updated.setSKU("SKU555");
        updated.setPrice(199.99);

        when(productService.updateProduct(
                Mockito.eq(1),
                any(ProductDTO.class),
                any(User.class)
        )).thenReturn(updated);

        mockMvc.perform(
                        patch("/api/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"));
    }
}
