package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import org.springframework.stereotype.Service;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;

@Service
public class WarehouseProductExchangeService {

    private final IWarehouseProductRepository warehouseProductRepository;
    private final IProductRepository productRepostiory;

    private final IWarehouseRepository warehouseRepository;

    public WarehouseProductExchangeService(IWarehouseProductRepository warehouseProductRepository) {
        this.warehouseProductRepository = warehouseProductRepository;
    public WarehouseProductExchangeService(IProductRepository productRepostiory, IWarehouseRepository warehouseRepository) {
        this.productRepostiory = productRepostiory;
        this.warehouseRepository = warehouseRepository;
    }

    public int getProductQuantity(Integer warehouseId, Integer productId) {
        return warehouseProductRepository
                .findByWarehouseIdAndProductId(warehouseId, productId)
                .map(WarehouseProduct::getQuantity)
                .orElse(0);
    }
}
