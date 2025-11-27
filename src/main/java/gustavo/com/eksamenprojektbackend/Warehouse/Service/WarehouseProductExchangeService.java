package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import org.springframework.stereotype.Service;

@Service
public class WarehouseProductExchangeService {

    private final IWarehouseProductRepository warehouseProductRepository;

    public WarehouseProductExchangeService(IWarehouseProductRepository warehouseProductRepository) {
        this.warehouseProductRepository = warehouseProductRepository;
    }

    public int getProductQuantity(Integer warehouseId, Integer productId) {
        return warehouseProductRepository
                .findByWarehouseIdAndProductId(warehouseId, productId)
                .map(WarehouseProduct::getQuantity)
                .orElse(0);
    }
}
