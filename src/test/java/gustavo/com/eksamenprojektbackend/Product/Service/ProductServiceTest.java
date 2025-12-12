package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private IProductRepository productRepository;
    private LogService logService;
    private IWarehouseProductRepository warehouseProductRepository;
    private IWarehouseRepository warehouseRepository;
    private ProductService productService;
    private ILogRepository iLogRepository;

    @BeforeEach
    void setup() {
        productRepository = mock(IProductRepository.class);
        logService = mock(LogService.class);
        warehouseProductRepository = mock(IWarehouseProductRepository.class);
        warehouseRepository = mock(IWarehouseRepository.class);

        productService = new ProductService(
                productRepository, logService, warehouseProductRepository, warehouseRepository, iLogRepository
        );
    }

    @Test
    void createProduct() {
        // Arrange
        ProductDTO dto = new ProductDTO(
                2,
                "Keyboard",
                "Cool keyboard",
                "pic.jpg",
                "SKU-1",
                799.0
        );

        User user = new User();
        user.setId(99);

        Product savedProduct = new Product(
                "Keyboard",
                "Cool keyboard",
                "pic.jpg",
                "SKU-1",
                799.0,
                new ArrayList<>()
        );
        savedProduct.setId(1);

        // Mock repositories
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(warehouseRepository.findAll()).thenReturn(List.of(new Warehouse(), new Warehouse()));

        // Act
        ProductDTO result = productService.createProduct(dto, user);

        // Assert
        assertEquals(1, result.id());
        assertEquals("Keyboard", result.name());
        assertEquals(799.0, result.price());

        verify(productRepository).save(any(Product.class));
        verify(warehouseProductRepository).saveAll(anyList());
        verify(logService).createLogFromProduct(any(), eq(user), contains("Oprettet"));
    }

    @Test
    void updateProduct() {
        // Arrange
        Product existing = new Product(
                "OldName",
                "OldDesc",
                "oldPic",
                "OLD-SKU",
                500.0,
                new ArrayList<>()
        );
        existing.setId(1);

        ProductDTO update = new ProductDTO(
                2,
                "NewName",
                "NewDesc",
                "newPic",
                "NEW-SKU",
                1000.0
        );

        User user = new User();

        when(productRepository.findById(1)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        // Act
        Product updated = productService.updateProduct(1, update, user);

        // Assert
        assertEquals("NewName", updated.getName());
        assertEquals("NewDesc", updated.getDescription());
        assertEquals("NEW-SKU", updated.getSKU());
        assertEquals(1000.0, updated.getPrice());

        verify(productRepository).save(any(Product.class));
        verify(logService).createLogFromProduct(eq(existing), eq(user), contains("ændret"));
    }
}
