package gustavo.com.eksamenprojektbackend.Warehouse.Service;


import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WarehouseService {

    private final IWarehouseRepository warehouseRepository;

    public WarehouseService(IWarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
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

}
