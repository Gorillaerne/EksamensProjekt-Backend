package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.PatchWarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseProductExchangeServiceTest {

    @Mock
    private IWarehouseProductRepository warehouseProductRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IWarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseProductExchangeService exchangeService;

    private Warehouse testWarehouse;
    private Product testProduct;
    private WarehouseProduct testWarehouseProduct;
    private WarehouseProductId testWarehouseProductId;

    @BeforeEach
    void setUp() {
        testWarehouse = new Warehouse();
        testWarehouse.setId(1);
        testWarehouse.setName("Test Warehouse");

        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");

        testWarehouseProductId = new WarehouseProductId(1, 1);

        testWarehouseProduct = new WarehouseProduct();
        testWarehouseProduct.setId(testWarehouseProductId);
        testWarehouseProduct.setWarehouse(testWarehouse);
        testWarehouseProduct.setProduct(testProduct);
        testWarehouseProduct.setQuantity(100);
    }

    @Test
    void getProductQuantity_ReturnsQuantityWhenProductExists() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));

        // Act
        int result = exchangeService.getProductQuantity(1, 1);

        // Assert
        assertEquals(100, result);
        verify(warehouseProductRepository).findByWarehouseIdAndProductId(1, 1);
    }

    @Test
    void getProductQuantity_ReturnsZeroWhenProductDoesNotExist() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(999, 999))
                .thenReturn(Optional.empty());

        // Act
        int result = exchangeService.getProductQuantity(999, 999);

        // Assert
        assertEquals(0, result);
        verify(warehouseProductRepository).findByWarehouseIdAndProductId(999, 999);
    }

    @Test
    void getProductQuantity_HandlesNullWarehouse() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(null, 1))
                .thenReturn(Optional.empty());

        // Act
        int result = exchangeService.getProductQuantity(null, 1);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void getProductQuantity_HandlesNullProduct() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, null))
                .thenReturn(Optional.empty());

        // Act
        int result = exchangeService.getProductQuantity(1, null);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void patchWarehouseProduct_Success() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 150);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        Object result = exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        assertNotNull(result);
        assertEquals(150, testWarehouseProduct.getQuantity());
        verify(warehouseProductRepository).findById(testWarehouseProductId);
        verify(warehouseProductRepository).save(testWarehouseProduct);
    }

    @Test
    void patchWarehouseProduct_ThrowsExceptionWhenProductNotFound() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 150);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> exchangeService.patchWarehouseProduct(patchDTO));
        assertTrue(exception.getMessage().contains("Product not found"));
        verify(warehouseProductRepository, never()).save(any(WarehouseProduct.class));
    }

    @Test
    void patchWarehouseProduct_UpdatesQuantityToZero() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 0);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        assertEquals(0, testWarehouseProduct.getQuantity());
        verify(warehouseProductRepository).save(testWarehouseProduct);
    }

    @Test
    void patchWarehouseProduct_UpdatesQuantityToNegative() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, -10);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        assertEquals(-10, testWarehouseProduct.getQuantity());
        verify(warehouseProductRepository).save(testWarehouseProduct);
    }

    @Test
    void patchWarehouseProduct_ReturnsDTO() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 200);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        Object result = exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof PatchWarehouseProductDTO);
        assertEquals(patchDTO, result);
    }

    @Test
    void getProductQuantity_HandlesDifferentWarehouses() {
        // Arrange
        WarehouseProduct wp2 = new WarehouseProduct();
        wp2.setQuantity(50);
        
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.findByWarehouseIdAndProductId(2, 1))
                .thenReturn(Optional.of(wp2));

        // Act
        int result1 = exchangeService.getProductQuantity(1, 1);
        int result2 = exchangeService.getProductQuantity(2, 1);

        // Assert
        assertEquals(100, result1);
        assertEquals(50, result2);
    }

    @Test
    void patchWarehouseProduct_HandlesLargeQuantity() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 1000000);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        assertEquals(1000000, testWarehouseProduct.getQuantity());
    }

    @Test
    void getProductQuantity_MultipleCallsSameProduct() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));

        // Act
        int result1 = exchangeService.getProductQuantity(1, 1);
        int result2 = exchangeService.getProductQuantity(1, 1);

        // Assert
        assertEquals(result1, result2);
        verify(warehouseProductRepository, times(2)).findByWarehouseIdAndProductId(1, 1);
    }

    @Test
    void patchWarehouseProduct_VerifiesSaveIsCalled() {
        // Arrange
        PatchWarehouseProductDTO patchDTO = new PatchWarehouseProductDTO(testWarehouseProductId, 75);
        
        when(warehouseProductRepository.findById(testWarehouseProductId))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseProductRepository.save(any(WarehouseProduct.class)))
                .thenReturn(testWarehouseProduct);

        // Act
        exchangeService.patchWarehouseProduct(patchDTO);

        // Assert
        verify(warehouseProductRepository).save(argThat(wp -> wp.getQuantity() == 75));
    }

    @Test
    void getProductQuantity_ReturnsZeroForNonExistentWarehouseProductCombination() {
        // Arrange
        when(warehouseProductRepository.findByWarehouseIdAndProductId(5, 10))
                .thenReturn(Optional.empty());

        // Act
        int result = exchangeService.getProductQuantity(5, 10);

        // Assert
        assertEquals(0, result);
    }
}