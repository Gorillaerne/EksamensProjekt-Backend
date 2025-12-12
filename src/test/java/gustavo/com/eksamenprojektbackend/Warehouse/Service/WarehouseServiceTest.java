package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions.WareHouseNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions.WareHouseUpdateException;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    void createWarehouse() {
        // Arrange
        WarehouseCreateDTO dto = new WarehouseCreateDTO("Test Lager", "Testvej 1", "Et lager");

        User user = new User();
        user.setUsername("tester");

        // Warehouse saved stub
        Warehouse savedWarehouse = new Warehouse("Test Lager", "Testvej 1", "Et lager");
        savedWarehouse.setId(1);

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(savedWarehouse);

        // Mock product list
        Product p = new Product();
        p.setId(10);
        p.setName("Test Produkt");
        p.setPrice(100.0);
        p.setDescription("Beskrivelse");
        p.setSKU("SKU123");

        when(productRepository.findAll()).thenReturn(List.of(p));

        // Act
        Warehouse result = warehouseService.createWarehouse(dto, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test Lager");

        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
        verify(warehouseProductRepository, times(1)).saveAll(anyList());
        verify(logService, times(1)).createLogFromUser(eq(user), contains("Har oprettet et nyt lager"));
    }

    @Test
    void updateWarehouse() {
        // Arrange
        Integer id = 1;

        Warehouse existing = new Warehouse("Old Name", "Oldvej", "Old desc");
        existing.setId(id);

        WarehouseDTO dto = new WarehouseDTO(
                1,
                "New Name",
                "Newvej",
                "New desc"
        );

        User user = new User();
        user.setUsername("tester");

        when(warehouseRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        Warehouse result = warehouseService.updateWarehouse(id, dto, user);

        // Assert
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getAddress()).isEqualTo("Newvej");
        assertThat(result.getDescription()).isEqualTo("New desc");

        verify(warehouseRepository, times(1)).save(existing);
        verify(logService, times(1))
                .createLogFromUser(eq(user), contains("Navn ændret fra Old Name"));
    }
}