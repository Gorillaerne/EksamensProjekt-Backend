package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseFrontendDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseProductDTO;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private IWarehouseRepository warehouseRepository;

    @Mock
    private IWarehouseProductRepository warehouseProductRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private WarehouseService warehouseService;

    private User testUser;
    private Warehouse testWarehouse;
    private Warehouse targetWarehouse;
    private Product testProduct;
    private WarehouseProduct testWarehouseProduct;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testWarehouse = new Warehouse();
        testWarehouse.setId(1);
        testWarehouse.setName("Test Warehouse");
        testWarehouse.setAddress("Test Location");
        testWarehouse.setDescription("Test Description");

        targetWarehouse = new Warehouse();
        targetWarehouse.setId(2);
        targetWarehouse.setName("Target Warehouse");
        targetWarehouse.setAddress("Target Location");
        targetWarehouse.setDescription("Target Description");

        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setSKU("TEST-001");
        testProduct.setPrice(99.99);

        testWarehouseProduct = new WarehouseProduct();
        testWarehouseProduct.setId(new WarehouseProductId(1, 1));
        testWarehouseProduct.setWarehouse(testWarehouse);
        testWarehouseProduct.setProduct(testProduct);
        testWarehouseProduct.setQuantity(100);
    }

    @Test
    void createWarehouse_Success() {
        // Arrange
        WarehouseCreateDTO createDTO = new WarehouseCreateDTO("New Warehouse", "Location", "Description");
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);

        // Act
        Warehouse result = warehouseService.createWarehouse(createDTO, testUser);

        // Assert
        assertNotNull(result);
        verify(warehouseRepository).save(any(Warehouse.class));
        verify(logService).createLogFromUser(eq(testUser), anyString());
    }

    @Test
    void getAllWarehouses_ReturnsAllWarehouses() {
        // Arrange
        when(warehouseRepository.findAll()).thenReturn(Arrays.asList(testWarehouse));

        // Act
        List<Warehouse> result = warehouseService.getAllWarehouses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Warehouse", result.get(0).getName());
        verify(warehouseRepository).findAll();
    }

    @Test
    void getWarehouseById_Success() {
        // Arrange
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));

        // Act
        Optional<Warehouse> result = warehouseService.getWarehouseById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Warehouse", result.get().getName());
        verify(warehouseRepository).findById(1);
    }

    @Test
    void getWarehouseById_ReturnsEmptyWhenNotFound() {
        // Arrange
        when(warehouseRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Warehouse> result = warehouseService.getWarehouseById(999);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateWarehouse_Success() {
        // Arrange
        Warehouse updateRequest = new Warehouse();
        updateRequest.setName("Updated Name");
        updateRequest.setAddress("Updated Location");
        updateRequest.setDescription("Updated Description");

        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);

        // Act
        Warehouse result = warehouseService.updateWarehouse(1, updateRequest, testUser);

        // Assert
        assertNotNull(result);
        verify(warehouseRepository).save(testWarehouse);
        verify(logService).createLogFromUser(eq(testUser), anyString());
    }

    @Test
    void updateWarehouse_ThrowsExceptionWhenWarehouseNotFound() {
        // Arrange
        Warehouse updateRequest = new Warehouse();
        when(warehouseRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> warehouseService.updateWarehouse(999, updateRequest, testUser));
    }

    @Test
    void getAllWarehousesForDelivery_ReturnsWarehouseFrontendDTOList() {
        // Arrange
        when(warehouseRepository.findAll()).thenReturn(Arrays.asList(testWarehouse));

        // Act
        List<WarehouseFrontendDTO> result = warehouseService.getAllWarehousesForDelivery();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Warehouse", result.get(0).name());
        verify(warehouseRepository).findAll();
    }

    @Test
    void moveProduct_Success() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 20);
        
        WarehouseProduct targetWarehouseProduct = new WarehouseProduct();
        targetWarehouseProduct.setId(new WarehouseProductId(2, 1));
        targetWarehouseProduct.setWarehouse(targetWarehouse);
        targetWarehouseProduct.setProduct(testProduct);
        targetWarehouseProduct.setQuantity(30);

        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseRepository.findById(2)).thenReturn(Optional.of(targetWarehouse));
        when(warehouseProductRepository.findByWarehouseIdAndProductId(2, 1))
                .thenReturn(Optional.of(targetWarehouseProduct));

        // Act
        WarehouseProductExchangeDTO result = warehouseService.moveProduct(request, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(80, testWarehouseProduct.getQuantity()); // 100 - 20
        assertEquals(50, targetWarehouseProduct.getQuantity()); // 30 + 20
        verify(warehouseProductRepository, times(2)).save(any(WarehouseProduct.class));
        verify(logService).createLogFromUser(eq(testUser), anyString());
    }

    @Test
    void moveProduct_ThrowsExceptionWhenSourceProductNotFound() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 20);
        
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> warehouseService.moveProduct(request, testUser));
        assertTrue(exception.getMessage().contains("Produkt findes ikke på kilde-lager"));
    }

    @Test
    void moveProduct_ThrowsExceptionWhenInsufficientStock() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 200);
        
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> warehouseService.moveProduct(request, testUser));
        assertTrue(exception.getMessage().contains("Ikke nok på lager til flytning"));
    }

    @Test
    void moveProduct_ThrowsExceptionWhenTargetWarehouseNotFound() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 999, 20);
        
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> warehouseService.moveProduct(request, testUser));
        assertTrue(exception.getMessage().contains("Mål-lager findes ikke"));
    }

    @Test
    void moveProduct_CreatesTargetWarehouseProductIfNotExists() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 20);
        
        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseRepository.findById(2)).thenReturn(Optional.of(targetWarehouse));
        when(warehouseProductRepository.findByWarehouseIdAndProductId(2, 1))
                .thenReturn(Optional.empty());

        // Act
        WarehouseProductExchangeDTO result = warehouseService.moveProduct(request, testUser);

        // Assert
        assertNotNull(result);
        verify(warehouseProductRepository, times(2)).save(any(WarehouseProduct.class));
    }

    @Test
    void getListOfProductsLowOnQty_ReturnsProductsBelowThreshold() {
        // Arrange
        testWarehouseProduct.setQuantity(30); // Below threshold of 50
        when(warehouseProductRepository.findAll()).thenReturn(Arrays.asList(testWarehouseProduct));

        // Act
        List<WarehouseProductDTO> result = warehouseService.getListOfProductsLowOnQty();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).productName());
        assertEquals(30, result.get(0).quantity());
    }

    @Test
    void getListOfProductsLowOnQty_ExcludesProductsAboveThreshold() {
        // Arrange
        testWarehouseProduct.setQuantity(100); // Above threshold of 50
        when(warehouseProductRepository.findAll()).thenReturn(Arrays.asList(testWarehouseProduct));

        // Act
        List<WarehouseProductDTO> result = warehouseService.getListOfProductsLowOnQty();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getListOfProductsLowOnQty_HandlesEmptyList() {
        // Arrange
        when(warehouseProductRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<WarehouseProductDTO> result = warehouseService.getListOfProductsLowOnQty();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getListOfProductsLowOnQty_IncludesProductAtExactThreshold() {
        // Arrange
        testWarehouseProduct.setQuantity(49); // Just below threshold
        when(warehouseProductRepository.findAll()).thenReturn(Arrays.asList(testWarehouseProduct));

        // Act
        List<WarehouseProductDTO> result = warehouseService.getListOfProductsLowOnQty();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getAllWarehouseProduct_ReturnsAllWarehouseProducts() {
        // Arrange
        when(warehouseProductRepository.findAll()).thenReturn(Arrays.asList(testWarehouseProduct));

        // Act
        List<WarehouseProduct> result = warehouseService.getAllWarehouseProduct();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(warehouseProductRepository).findAll();
    }

    @Test
    void getAllWarehouses_ReturnsEmptyListWhenNoWarehouses() {
        // Arrange
        when(warehouseRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Warehouse> result = warehouseService.getAllWarehouses();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void moveProduct_HandlesExactStockAmount() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 100);
        
        WarehouseProduct targetWarehouseProduct = new WarehouseProduct();
        targetWarehouseProduct.setId(new WarehouseProductId(2, 1));
        targetWarehouseProduct.setWarehouse(targetWarehouse);
        targetWarehouseProduct.setProduct(testProduct);
        targetWarehouseProduct.setQuantity(50);

        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseRepository.findById(2)).thenReturn(Optional.of(targetWarehouse));
        when(warehouseProductRepository.findByWarehouseIdAndProductId(2, 1))
                .thenReturn(Optional.of(targetWarehouseProduct));

        // Act
        warehouseService.moveProduct(request, testUser);

        // Assert
        assertEquals(0, testWarehouseProduct.getQuantity()); // All moved
        assertEquals(150, targetWarehouseProduct.getQuantity()); // 50 + 100
    }

    @Test
    void moveProduct_VerifiesLogContent() {
        // Arrange
        WarehouseProductExchangeDTO request = new WarehouseProductExchangeDTO(1, 1, 2, 20);
        
        WarehouseProduct targetWarehouseProduct = new WarehouseProduct();
        targetWarehouseProduct.setId(new WarehouseProductId(2, 1));
        targetWarehouseProduct.setWarehouse(targetWarehouse);
        targetWarehouseProduct.setProduct(testProduct);
        targetWarehouseProduct.setQuantity(30);

        when(warehouseProductRepository.findByWarehouseIdAndProductId(1, 1))
                .thenReturn(Optional.of(testWarehouseProduct));
        when(warehouseRepository.findById(2)).thenReturn(Optional.of(targetWarehouse));
        when(warehouseProductRepository.findByWarehouseIdAndProductId(2, 1))
                .thenReturn(Optional.of(targetWarehouseProduct));

        // Act
        warehouseService.moveProduct(request, testUser);

        // Assert
        verify(logService).createLogFromUser(eq(testUser), 
                argThat(msg -> msg.contains("flyttet") && 
                               msg.contains("20") && 
                               msg.contains("Test Product")));
    }

    @Test
    void getAllWarehousesForDelivery_HandlesMultipleWarehouses() {
        // Arrange
        when(warehouseRepository.findAll()).thenReturn(Arrays.asList(testWarehouse, targetWarehouse));

        // Act
        List<WarehouseFrontendDTO> result = warehouseService.getAllWarehousesForDelivery();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(w -> w.name().equals("Test Warehouse")));
        assertTrue(result.stream().anyMatch(w -> w.name().equals("Target Warehouse")));
    }
}