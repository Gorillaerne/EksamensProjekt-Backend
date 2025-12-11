package gustavo.com.eksamenprojektbackend.Logs.Service;

import gustavo.com.eksamenprojektbackend.Logs.Model.Log;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private ILogRepository logRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private LogService logService;

    private User testUser;
    private Product testProduct;
    private Log testLog;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");

        testLog = new Log();
        testLog.setId(1);
        testLog.setUser(testUser);
        testLog.setProduct(testProduct);
        testLog.setAction("Test action");
        testLog.setTimeStamp(LocalDateTime.now());
    }

    @Test
    void getAll_ReturnsAllLogs() {
        // Arrange
        Log log2 = new Log();
        log2.setId(2);
        log2.setUser(testUser);
        log2.setAction("Another action");
        
        when(logRepository.findAll()).thenReturn(Arrays.asList(testLog, log2));

        // Act
        List<Log> result = logService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(logRepository).findAll();
    }

    @Test
    void getAll_ReturnsEmptyListWhenNoLogs() {
        // Arrange
        when(logRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Log> result = logService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getLogsByUserID_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(logRepository.findByUserId(1)).thenReturn(Arrays.asList(testLog));

        // Act
        List<Log> result = logService.getLogsByUserID(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test action", result.get(0).getAction());
        verify(userRepository).findById(1);
        verify(logRepository).findByUserId(1);
    }

    @Test
    void getLogsByUserID_ThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> logService.getLogsByUserID(999));
        assertTrue(exception.getMessage().contains("User not found"));
        verify(logRepository, never()).findByUserId(anyInt());
    }

    @Test
    void getLogsByProductID_ReturnsLogsList() {
        // Arrange
        when(logRepository.findByProductId(1)).thenReturn(Arrays.asList(testLog));

        // Act
        List<Log> result = logService.getLogsByProductID(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct, result.get(0).getProduct());
        verify(logRepository).findByProductId(1);
    }

    @Test
    void getLogsByProductID_ReturnsEmptyListWhenNoLogs() {
        // Arrange
        when(logRepository.findByProductId(999)).thenReturn(Arrays.asList());

        // Act
        List<Log> result = logService.getLogsByProductID(999);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void createLogFromUser_Success() {
        // Arrange
        String message = "User performed an action";
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> {
            Log log = invocation.getArgument(0);
            log.setId(2);
            return log;
        });

        // Act
        Log result = logService.createLogFromUser(testUser, message);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(message, result.getAction());
        assertNotNull(result.getTimeStamp());
        verify(logRepository).save(any(Log.class));
    }

    @Test
    void createLogFromUser_SetsTimestamp() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Log result = logService.createLogFromUser(testUser, "Test message");
        LocalDateTime afterCreation = LocalDateTime.now();

        // Assert
        assertNotNull(result.getTimeStamp());
        assertTrue(result.getTimeStamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(result.getTimeStamp().isBefore(afterCreation.plusSeconds(1)));
    }

    @Test
    void createLogFromProduct_Success() {
        // Arrange
        String message = "Product action performed";
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> {
            Log log = invocation.getArgument(0);
            log.setId(3);
            return log;
        });

        // Act
        Log result = logService.createLogFromProduct(testProduct, testUser, message);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct, result.getProduct());
        assertEquals(testUser, result.getUser());
        assertEquals(message, result.getAction());
        assertNotNull(result.getTimeStamp());
        verify(logRepository).save(any(Log.class));
    }

    @Test
    void createLogFromProduct_SetsAllFields() {
        // Arrange
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Log result = logService.createLogFromProduct(testProduct, testUser, "Product updated");

        // Assert
        assertNotNull(result.getProduct());
        assertNotNull(result.getUser());
        assertNotNull(result.getAction());
        assertNotNull(result.getTimeStamp());
        assertEquals("Product updated", result.getAction());
    }

    @Test
    void createLogFromProduct_SavesLogToRepository() {
        // Arrange
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        logService.createLogFromProduct(testProduct, testUser, "Test");

        // Assert
        verify(logRepository).save(argThat(log ->
                log.getProduct().equals(testProduct) &&
                log.getUser().equals(testUser) &&
                log.getAction().equals("Test") &&
                log.getTimeStamp() != null
        ));
    }

    @Test
    void createLogFromUser_HandlesLongMessage() {
        // Arrange
        String longMessage = "A".repeat(1000);
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Log result = logService.createLogFromUser(testUser, longMessage);

        // Assert
        assertNotNull(result);
        assertEquals(longMessage, result.getAction());
    }

    @Test
    void createLogFromUser_HandlesEmptyMessage() {
        // Arrange
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Log result = logService.createLogFromUser(testUser, "");

        // Assert
        assertNotNull(result);
        assertEquals("", result.getAction());
    }

    @Test
    void getLogsByUserID_ReturnsMultipleLogs() {
        // Arrange
        Log log2 = new Log();
        log2.setId(2);
        log2.setUser(testUser);
        log2.setAction("Second action");
        
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(logRepository.findByUserId(1)).thenReturn(Arrays.asList(testLog, log2));

        // Act
        List<Log> result = logService.getLogsByUserID(1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void createLogFromProduct_HandlesNullProduct() {
        // Arrange
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Log result = logService.createLogFromProduct(null, testUser, "Test");

        // Assert
        assertNull(result.getProduct());
        assertNotNull(result.getUser());
    }

    @Test
    void getLogsByProductID_HandlesMultipleUsers() {
        // Arrange
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        
        Log log2 = new Log();
        log2.setId(2);
        log2.setProduct(testProduct);
        log2.setUser(user2);
        
        when(logRepository.findByProductId(1)).thenReturn(Arrays.asList(testLog, log2));

        // Act
        List<Log> result = logService.getLogsByProductID(1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getUser().getUsername().equals("testuser")));
        assertTrue(result.stream().anyMatch(l -> l.getUser().getUsername().equals("user2")));
    }
}