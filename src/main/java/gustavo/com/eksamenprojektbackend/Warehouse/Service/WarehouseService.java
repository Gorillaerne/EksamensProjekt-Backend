package gustavo.com.eksamenprojektbackend.Warehouse.Service;


import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WarehouseService {

    private final IWarehouseRepository warehouseRepository;
    private final IWarehouseProductRepository warehouseProductRepository;

    public WarehouseService(IWarehouseRepository warehouseRepository, IWarehouseProductRepository warehouseProductRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseProductRepository = warehouseProductRepository;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(int id){
        return warehouseRepository.findById(id);
    }


    public Warehouse updateWarehouse(Integer id, Warehouse updatedWarehouse) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse med id " + id + " blev ikke fundet"));

        existingWarehouse.setName(updatedWarehouse.getName());
        existingWarehouse.setAddress(updatedWarehouse.getAddress());
        existingWarehouse.setDescription(updatedWarehouse.getDescription());

        return warehouseRepository.save(existingWarehouse);
    }

    @Transactional
    public WarehouseProductExchangeDTO moveProduct(WarehouseProductExchangeDTO request) {

        WarehouseProduct source = warehouseProductRepository
                .findByWarehouse_IdAndProduct_Id(
                        request.getSourceWarehouseId(),
                        request.getProductId()
                )
                .orElseThrow(() -> new RuntimeException("Produkt findes ikke på kilde-lager"));

        if (source.getQuantity() < request.getAntal()) {
            throw new RuntimeException("Ikke nok på lager til flytning");
        }

        Warehouse targetWarehouse = warehouseRepository.findById(request.getTargetWarehouseId())
                .orElseThrow(() -> new RuntimeException("Mål-lager findes ikke"));

        WarehouseProduct target = warehouseProductRepository
                .findByWarehouse_IdAndProduct_Id(
                        request.getTargetWarehouseId(),
                        request.getProductId()
                )
                .orElseGet(() -> {
                    WarehouseProduct newWP = new WarehouseProduct();
                    newWP.setWarehouse(targetWarehouse);
                    newWP.setProduct(source.getProduct());
                    newWP.setId(new WarehouseProductId(
                            request.getTargetWarehouseId(),
                            request.getProductId()
                    ));
                    newWP.setQuantity(0);
                    return newWP;
                });

        // Flyt mængde
        source.setQuantity(source.getQuantity() - request.getAntal());
        target.setQuantity(target.getQuantity() + request.getAntal());

        warehouseProductRepository.save(source);
        warehouseProductRepository.save(target);

        request.setStatus("SUCCESS");
        return request;
    }

}
