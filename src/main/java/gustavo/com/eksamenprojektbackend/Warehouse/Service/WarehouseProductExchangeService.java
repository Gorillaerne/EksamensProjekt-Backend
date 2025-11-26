package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepostiory;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;

public class WarehouseProductExchangeService {

    private final IProductRepostiory productRepostiory;

    private final IWarehouseRepository warehouseRepository;

    public WarehouseProductExchangeService(IProductRepostiory productRepostiory, IWarehouseRepository warehouseRepository) {
        this.productRepostiory = productRepostiory;
        this.warehouseRepository = warehouseRepository;
    }


}
