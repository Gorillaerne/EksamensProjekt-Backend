package gustavo.com.eksamenprojektbackend.Warehouse.Service;


import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseFrontendDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WarehouseService {

    private final IWarehouseRepository warehouseRepository;
    private final IWarehouseProductRepository warehouseProductRepository;
    private final LogService logService;

    public WarehouseService(IWarehouseRepository warehouseRepository, IWarehouseProductRepository warehouseProductRepository, LogService logService) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.logService = logService;
    }

    public Warehouse createWarehouse(WarehouseCreateDTO warehouse, User user) {
        logService.createLogFromUser(user, "User: " + user.getUsername() +" | Har oprettet et nyt lager: " + warehouse.name());
        return warehouseRepository.save(new Warehouse(warehouse.name(), warehouse.address(), warehouse.description()));
    }

    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }
    public List<WarehouseFrontendDTO> getAllWarehousesForDelivery(){

        List<WarehouseFrontendDTO> dtoList = new ArrayList<>();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        for (Warehouse warehouse : warehouses){
            dtoList.add(new WarehouseFrontendDTO(warehouse.getId(),warehouse.getName()));
        }
        return dtoList;
    }

    public Optional<Warehouse> getWarehouseById(int id){
        return warehouseRepository.findById(id);
    }


    public Warehouse updateWarehouse(Integer id, Warehouse updatedWarehouse, User user) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse med id " + id + " blev ikke fundet"));

        existingWarehouse.setName(updatedWarehouse.getName());
        existingWarehouse.setAddress(updatedWarehouse.getAddress());
        existingWarehouse.setDescription(updatedWarehouse.getDescription());

        logService.createLogFromUser(user,
                "Ændret lageret fra: " + existingWarehouse.getName() + " | " + existingWarehouse.getAddress()  + " | " + existingWarehouse.getDescription() + " -> " +
                updatedWarehouse.getName() + " | " + updatedWarehouse.getAddress() + " | " + existingWarehouse.getDescription());

        return warehouseRepository.save(existingWarehouse);
    }

    @Transactional
    public WarehouseProductExchangeDTO moveProduct(WarehouseProductExchangeDTO request, User user) {

        // Hent produkt på afsendelseslageret
        WarehouseProduct source = warehouseProductRepository
                .findByWarehouseIdAndProductId(
                        request.getFromWarehouseId(),
                        request.getProductId()
                )
                .orElseThrow(() -> new RuntimeException("Produkt findes ikke på kilde-lager"));

        // Tjek om der er nok på lager
        if (source.getQuantity() < request.getAmount()) {
            throw new RuntimeException("Ikke nok på lager til flytning");
        }

        // Find mål-lager
        Warehouse targetWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new RuntimeException("Mål-lager findes ikke"));

        // Find produkt på mål-lager ellers opret det
        WarehouseProduct target = warehouseProductRepository
                .findByWarehouseIdAndProductId(
                        request.getToWarehouseId(),
                        request.getProductId()
                )
                .orElseGet(() -> {
                    WarehouseProduct wp = new WarehouseProduct();
                    wp.setWarehouse(targetWarehouse);
                    wp.setProduct(source.getProduct());
                    wp.setId(new WarehouseProductId(
                            request.getToWarehouseId(),
                            request.getProductId()
                    ));
                    wp.setQuantity(0);
                    return wp;
                });

        // Flyt antal
        source.setQuantity(source.getQuantity() - request.getAmount());
        target.setQuantity(target.getQuantity() + request.getAmount());

        warehouseProductRepository.save(source);
        warehouseProductRepository.save(target);

        logService.createLogFromUser(user,
                        "Har flyttet " + request.getAmount() +
                        " stk. af produkt '" + source.getProduct().getName() +
                        "' | Fra lager ID: " + request.getFromWarehouseId() +
                        " til lager ID: " + request.getToWarehouseId()
        );



        return request; // Kan evt. returnere en SUCCESS message
    }

    public List<WarehouseProduct> getAllWarehouseProduct() {
       return warehouseProductRepository.findAll();
    }

    public List<WarehouseProductDTO> getListOfProductsLowOnQty(){
       List<WarehouseProduct> wpList = getAllWarehouseProduct();
       List<WarehouseProductDTO> wpLowQtyList = new ArrayList<>();

       for(WarehouseProduct w : wpList) {
           if (w.getQuantity() < 50) {



               wpLowQtyList.add(new WarehouseProductDTO(w.getQuantity(),w.getProduct().getName(), w.getWarehouse().getName()));
           }
       }
       return wpLowQtyList;
    }

}
