package gustavo.com.eksamenprojektbackend.Product.DTO;

import java.util.List;

public record ProductWithWarehouseDTO(int id, String name, String description, String picture, String SKU, double price , List<ProductWarehouseDTO> warehouseList) {
}
