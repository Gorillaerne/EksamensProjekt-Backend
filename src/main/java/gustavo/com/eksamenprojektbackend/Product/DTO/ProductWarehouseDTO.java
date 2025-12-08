package gustavo.com.eksamenprojektbackend.Product.DTO;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;

public record ProductWarehouseDTO (WarehouseProductId id, int quantity, String name) {
}
