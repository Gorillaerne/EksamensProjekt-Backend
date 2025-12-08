package gustavo.com.eksamenprojektbackend.Warehouse.DTO;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;

import java.util.List;

public record PatchWarehouseProductDTO(WarehouseProductId id, int quantity) {
}
