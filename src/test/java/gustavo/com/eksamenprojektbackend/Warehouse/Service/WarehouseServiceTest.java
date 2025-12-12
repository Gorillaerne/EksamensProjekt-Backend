package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private IWarehouseRepository warehouseRepository;

    @Mock
    private IWarehouseProductRepository warehouseProductRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void createWarehouse_success() {
        // Arrange
        WarehouseCreateDTO dto = new WarehouseCreateDTO("Test Lager", "Testvej 1", "Et lager");
        User user = new User();
        user.setUsername("testUser");

        // Mock warehouse save
        Warehouse savedWarehouse = new Warehouse("Test Lager", "Testvej 1", "Et lager");
        savedWarehouse.setId(1); // ID sættes manuelt i tests
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(savedWarehouse);

        // Mock eksisterende produkter
        Product p = new Product();
        p.setId(10);
        p.setName("Test Produkt");
        p.setDescription("Beskrivelse");
        p.setPrice(100.0);
        p.setSKU("SKU123");

        when(productRepository.findAll()).thenReturn(List.of(p));

        // Act
        Warehouse result = warehouseService.createWarehouse(dto, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Lager");

        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
        verify(warehouseProductRepository, times(1)).saveAll(anyList());
        verify(logService, times(1)).createLogFromUser(any(), anyString());
    }

    @Test
    void updateWarehouse_success() {
        // Arrange
        Integer id = 1;

        Warehouse existing = new Warehouse("Old", "Oldvej", "Old desc");
        existing.setId(id);

        Warehouse updated = new Warehouse("New", "Newvej", "New desc");
        updated.setId(id);

        when(warehouseRepository.findById(id)).thenReturn(Optional.of(existing));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(updated);

        User user = new User();
        user.setUsername("testUser");

        // Act
        Warehouse result = warehouseService.updateWarehouse(id, updated, user);

        // Assert
        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getAddress()).isEqualTo("Newvej");

        verify(warehouseRepository, times(1)).save(existing);
        verify(logService, times(1)).createLogFromUser(any(), anyString());
    }
}
