package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Warehouse.DTO.PatchWarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import org.springframework.stereotype.Service;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;

import java.util.Map;

@Service
public class WarehouseProductExchangeService {

    private final IWarehouseProductRepository warehouseProductRepository;
    private final IProductRepository productRepostiory;
    private final IWarehouseRepository warehouseRepository;
    
    public WarehouseProductExchangeService(IWarehouseProductRepository warehouseProductRepository, IProductRepository productRepostiory, IWarehouseRepository warehouseRepository) {
        this.warehouseProductRepository = warehouseProductRepository;
        this.productRepostiory = productRepostiory;
        this.warehouseRepository = warehouseRepository;
    }

    public int getProductQuantity(Integer warehouseId, Integer productId) {
        return warehouseProductRepository
                .findByWarehouseIdAndProductId(warehouseId, productId)
                .map(WarehouseProduct::getQuantity)
                .orElse(0);
    }

    public Object patchWarehouseProduct(PatchWarehouseProductDTO dto) {

        WarehouseProduct wp = warehouseProductRepository.findById( dto.id()
        ) .orElseThrow(() -> new RuntimeException("Product not found with id "));;

        wp.setQuantity(dto.quantity());
        warehouseProductRepository.save(wp);
        return dto;
    }
}
