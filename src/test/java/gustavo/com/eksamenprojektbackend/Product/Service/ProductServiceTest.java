package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.DTO.*;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private LogService logService;

    @Mock
    private IWarehouseProductRepository warehouseProductRepository;

    @Mock
    private IWarehouseRepository warehouseRepository;

    @InjectMocks
    private ProductService productService;

    private User testUser;
    private Product testProduct;
    private Warehouse testWarehouse;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPicture("test.jpg");
        testProduct.setSKU("TEST-001");
        testProduct.setPrice(99.99);
        testProduct.setWarehouseProductList(new ArrayList<>());

        testWarehouse = new Warehouse();
        testWarehouse.setId(1);
        testWarehouse.setName("Test Warehouse");

        testProductDTO = new ProductDTO(1, "Test Product", "Test Description", "test.jpg", "TEST-001", 99.99);
    }

    @Test
    void createProduct_Success() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(warehouseRepository.findAll()).thenReturn(List.of(testWarehouse));
        when(warehouseProductRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        // Act
        ProductDTO result = productService.createProduct(testProductDTO, testUser);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.name());
        assertEquals("TEST-001", result.SKU());
        assertEquals(99.99, result.price());
        verify(productRepository).save(any(Product.class));
        verify(logService).createLogFromProduct(any(Product.class), eq(testUser), anyString());
        verify(warehouseProductRepository).saveAll(anyList());
    }

    @Test
    void createProduct_ThrowsExceptionOnError() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(Exception.class, () -> productService.createProduct(testProductDTO, testUser));
    }

    @Test
    void getAllProducts_ReturnsAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getAllProductsForSearchBar_ReturnsSearchBarDTOList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        // Act
        List<SearchBarProductDTO> result = productService.getAllProductsForSearchBar();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).name());
        assertEquals("TEST-001", result.get(0).SKU());
        verify(productRepository).findAll();
    }

    @Test
    void registerDeliveryOfGoods_Success_NewWarehouseProduct() {
        // Arrange
        RegisterDeliveryDTO deliveryDTO = new RegisterDeliveryDTO(1, 1, 10);
        List<RegisterDeliveryDTO> deliveryDTOS = Arrays.asList(deliveryDTO);
        
        WarehouseProductId wpId = new WarehouseProductId(1, 1);
        
        when(warehouseProductRepository.existsById(wpId)).thenReturn(false);
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        // Act
        ResponseDeliveryDTO result = productService.registerDeliveryOfGoods(deliveryDTOS, testUser);

        // Assert
        assertNotNull(result);
        assertEquals("Levering registreret", result.message());
        assertEquals(1, result.processedQuantity());
        verify(warehouseProductRepository).save(any(WarehouseProduct.class));
        verify(logService).createLogFromUser(eq(testUser), anyString());
    }

    @Test
    void registerDeliveryOfGoods_Success_ExistingWarehouseProduct() {
        // Arrange
        RegisterDeliveryDTO deliveryDTO = new RegisterDeliveryDTO(1, 1, 10);
        List<RegisterDeliveryDTO> deliveryDTOS = Arrays.asList(deliveryDTO);
        
        WarehouseProductId wpId = new WarehouseProductId(1, 1);
        WarehouseProduct existingWP = new WarehouseProduct();
        existingWP.setId(wpId);
        existingWP.setQuantity(5);
        existingWP.setWarehouse(testWarehouse);
        existingWP.setProduct(testProduct);
        
        when(warehouseProductRepository.existsById(wpId)).thenReturn(true);
        when(warehouseProductRepository.findById(wpId)).thenReturn(Optional.of(existingWP));

        // Act
        ResponseDeliveryDTO result = productService.registerDeliveryOfGoods(deliveryDTOS, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(15, existingWP.getQuantity());
        verify(warehouseProductRepository).save(existingWP);
    }

    @Test
    void registerDeliveryOfGoods_ThrowsExceptionWhenListIsEmpty() {
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.registerDeliveryOfGoods(new ArrayList<>(), testUser));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void registerDeliveryOfGoods_ThrowsExceptionWhenListIsNull() {
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.registerDeliveryOfGoods(null, testUser));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void registerDeliveryOfGoods_ThrowsExceptionWhenWarehouseNotFound() {
        // Arrange
        RegisterDeliveryDTO deliveryDTO = new RegisterDeliveryDTO(1, 1, 10);
        List<RegisterDeliveryDTO> deliveryDTOS = Arrays.asList(deliveryDTO);
        
        WarehouseProductId wpId = new WarehouseProductId(1, 1);
        when(warehouseProductRepository.existsById(wpId)).thenReturn(false);
        when(warehouseRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> productService.registerDeliveryOfGoods(deliveryDTOS, testUser));
    }

    @Test
    void updateProduct_Success_AllFieldsUpdated() {
        // Arrange
        Product updateRequest = new Product();
        updateRequest.setName("Updated Name");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPrice(199.99);
        updateRequest.setPicture("updated.jpg");
        updateRequest.setSKU("UPDATED-001");

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.updateProduct(1, updateRequest, testUser);

        // Assert
        assertNotNull(result);
        verify(productRepository).save(testProduct);
        verify(logService).createLogFromProduct(eq(testProduct), eq(testUser), anyString());
    }

    @Test
    void updateProduct_ThrowsExceptionWhenProductNotFound() {
        // Arrange
        Product updateRequest = new Product();
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> productService.updateProduct(1, updateRequest, testUser));
    }

    @Test
    void updateProduct_OnlyUpdatesProvidedFields() {
        // Arrange
        Product updateRequest = new Product();
        updateRequest.setName("Updated Name");
        // Other fields are null

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.updateProduct(1, updateRequest, testUser);

        // Assert
        assertNotNull(result);
        verify(productRepository).save(testProduct);
    }

    @Test
    void getProductById_Success() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = productService.getProductById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(1);
    }

    @Test
    void getProductById_ThrowsExceptionWhenNotFound() {
        // Arrange
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.getProductById(999));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getAllProductsDto_ReturnsProductDTOListWithQuantities() {
        // Arrange
        WarehouseProduct wp1 = new WarehouseProduct();
        wp1.setQuantity(10);
        WarehouseProduct wp2 = new WarehouseProduct();
        wp2.setQuantity(15);
        
        testProduct.setWarehouseProductList(Arrays.asList(wp1, wp2));
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        // Act
        List<ProductDTO> result = productService.getAllProductsDto();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(25, result.get(0).quantity()); // 10 + 15
        assertEquals("Test Product", result.get(0).name());
    }

    @Test
    void getAllProductsDto_HandlesEmptyWarehouseList() {
        // Arrange
        testProduct.setWarehouseProductList(new ArrayList<>());
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        // Act
        List<ProductDTO> result = productService.getAllProductsDto();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).quantity());
    }

    @Test
    void getProductWithWarehouseDTO_Success() {
        // Arrange
        WarehouseProduct wp = new WarehouseProduct();
        wp.setId(new WarehouseProductId(1, 1));
        wp.setQuantity(10);
        wp.setWarehouse(testWarehouse);
        
        testProduct.setWarehouseProductList(Arrays.asList(wp));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        // Act
        ProductWithWarehouseDTO result = productService.getProductWithWarehouseDTO(1);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.name());
        assertEquals(1, result.warehouseList().size());
        assertEquals(10, result.warehouseList().get(0).quantity());
        assertEquals("Test Warehouse", result.warehouseList().get(0).name());
    }

    @Test
    void getProductWithWarehouseDTO_ThrowsExceptionWhenProductNotFound() {
        // Arrange
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> productService.getProductWithWarehouseDTO(999));
    }

    @Test
    void registerDeliveryOfGoods_HandlesMultipleDeliveries() {
        // Arrange
        RegisterDeliveryDTO delivery1 = new RegisterDeliveryDTO(1, 1, 10);
        RegisterDeliveryDTO delivery2 = new RegisterDeliveryDTO(1, 2, 20);
        List<RegisterDeliveryDTO> deliveryDTOS = Arrays.asList(delivery1, delivery2);
        
        WarehouseProductId wpId1 = new WarehouseProductId(1, 1);
        WarehouseProductId wpId2 = new WarehouseProductId(1, 2);
        
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        
        when(warehouseProductRepository.existsById(any())).thenReturn(false);
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(testWarehouse));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));

        // Act
        ResponseDeliveryDTO result = productService.registerDeliveryOfGoods(deliveryDTOS, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.processedQuantity());
        verify(warehouseProductRepository, times(2)).save(any(WarehouseProduct.class));
        verify(logService, times(2)).createLogFromUser(eq(testUser), anyString());
    }
}