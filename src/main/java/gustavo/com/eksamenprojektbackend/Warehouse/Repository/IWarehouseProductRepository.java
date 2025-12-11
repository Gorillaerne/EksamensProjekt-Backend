package gustavo.com.eksamenprojektbackend.Warehouse.Repository;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IWarehouseProductRepository extends JpaRepository<WarehouseProduct, WarehouseProductId> {

    Optional<WarehouseProduct> findByWarehouseIdAndProductId(Integer warehouseId, Integer productId);

    List<WarehouseProduct> findAllProductsByWarehouseId(Integer warehouseId);
}


