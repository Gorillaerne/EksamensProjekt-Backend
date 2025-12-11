package gustavo.com.eksamenprojektbackend.Warehouse.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductFetchException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductNotFoundException;
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
        if (warehouseId == null || productId == null) {
            throw new WarehouseProductFetchException("warehouseId og productId må ikke være null");
        }

        return warehouseProductRepository
                .findByWarehouseIdAndProductId(warehouseId, productId)
                .map(WarehouseProduct::getQuantity)
                .orElseThrow(() -> new WarehouseProductNotFoundException("Warehouse id : " + warehouseId +  " Product id: " + productId));
    }

    public PatchWarehouseProductDTO patchWarehouseProduct(PatchWarehouseProductDTO dto) {
        if (dto == null || dto.id() == null) {
            throw new WarehouseProductFetchException("DTO eller ID må ikke være null");
        }

        if (dto.quantity() < 0) {
            throw new WarehouseProductFetchException("Quantity må ikke være negativ");
        }

        WarehouseProduct wp = warehouseProductRepository.findById(dto.id())
                .orElseThrow(() -> new WarehouseProductNotFoundException(dto.id().toString()));

        wp.setQuantity(dto.quantity());
        warehouseProductRepository.save(wp);

        return dto;
    }
}
