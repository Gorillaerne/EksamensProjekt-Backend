package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;

public class WarehouseProductExchangeService {

    private final IProductRepository productRepostiory;

    private final IWarehouseRepository warehouseRepository;

    public WarehouseProductExchangeService(IProductRepository productRepostiory, IWarehouseRepository warehouseRepository) {
        this.productRepostiory = productRepostiory;
        this.warehouseRepository = warehouseRepository;
    }


}
