package gustavo.com.eksamenprojektbackend.Warehouse.Repository;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IWarehouseProductRepository extends JpaRepository<WarehouseProduct, WarehouseProductId> {

    Optional<WarehouseProduct> findByWarehouse_IdAndProduct_Id(Integer warehouseId, Integer productId);
}

