package gustavo.com.eksamenprojektbackend.Warehouse.Service;


import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions.*;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductFetchException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductNotFoundException;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
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
    private final IProductRepository iProductRepository;


    public WarehouseService(IWarehouseRepository warehouseRepository, IWarehouseProductRepository warehouseProductRepository, LogService logService, IProductRepository iProductRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.logService = logService;
        this.iProductRepository = iProductRepository;
    }

    public Warehouse createWarehouse(WarehouseCreateDTO warehouseDTO, User user) {
        if (warehouseDTO == null || warehouseDTO.name() == null || warehouseDTO.address() == null) {
            throw new WareHouseCreationException("Warehouse DTO, name eller address må ikke være null");
        }
        try {

            Warehouse savedWarehouse = warehouseRepository.save(
                    new Warehouse(warehouseDTO.name(), warehouseDTO.address(), warehouseDTO.description())
            );

            List<Product> products = iProductRepository.findAll();
            List<WarehouseProduct> wpList = new ArrayList<>();
            for (Product product : products) {
                wpList.add(new WarehouseProduct(savedWarehouse, product, 0));
            }

            warehouseProductRepository.saveAll(wpList);
            logService.createLogFromUser(user, "Har oprettet et nyt lager: " + savedWarehouse.getName());

            return savedWarehouse;

        } catch (LogException e) {
            throw new WareHouseCreationException("Fejl under oprettelse af logs: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new WareHouseCreationException("Fejl under oprettelse af lager: " + e.getMessage(), e);
        }
    }

    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    public List<WarehouseFrontendDTO> getAllWarehousesForDelivery() {
        try {
            List<Warehouse> warehouses = warehouseRepository.findAll();


            List<WarehouseFrontendDTO> dtoList = new ArrayList<>();
            for (Warehouse warehouse : warehouses) {
                dtoList.add(new WarehouseFrontendDTO(warehouse.getId(), warehouse.getName()));
            }

            return dtoList;

        } catch (Exception e) {
            throw new WareHouseFetchException("Fejl under hentning af lagre", e);
        }
    }


    public Warehouse getWarehouseById(Integer id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new WareHouseNotFoundException(" id: " + id));
    }


    public Warehouse updateWarehouse(Integer id, Warehouse updatedWarehouse, User user) {
        if (updatedWarehouse == null) {
            throw new WareHouseUpdateException("Opdateringsdata må ikke være null");
        }


        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new WareHouseNotFoundException("id : " + id));


        String oldName = existingWarehouse.getName();
        String oldAddress = existingWarehouse.getAddress();
        String oldDescription = existingWarehouse.getDescription();


        existingWarehouse.setName(updatedWarehouse.getName());
        existingWarehouse.setAddress(updatedWarehouse.getAddress());
        existingWarehouse.setDescription(updatedWarehouse.getDescription());

        try {
            Warehouse savedWarehouse = warehouseRepository.save(existingWarehouse);

            logService.createLogFromUser(user, "Ændret lageret fra: " + oldName + " | " + oldAddress + " | " + oldDescription +
                            " -> " + updatedWarehouse.getName() + " | " + updatedWarehouse.getAddress() + " | " + updatedWarehouse.getDescription());

            return savedWarehouse;

        } catch (LogException e) {
            throw new WareHouseUpdateException("Fejl under oprettelse af logs", e);
        }catch (Exception e) {
            throw new WareHouseUpdateException("Fejl under opdatering af lager: " + e.getMessage(), e);
        }
    }

    @Transactional
    public WarehouseProductExchangeDTO moveProduct(WarehouseProductExchangeDTO request, User user) {

        try {
            // Hent produkt på afsendelseslageret
            WarehouseProduct source = warehouseProductRepository
                    .findByWarehouseIdAndProductId(request.getFromWarehouseId(), request.getProductId())
                    .orElseThrow(() -> new WarehouseProductNotFoundException(
                            "Produkt findes ikke på kilde-lager ID: " + request.getFromWarehouseId())
                    );

            // Tjek om der er nok på lager
            if (source.getQuantity() < request.getAmount()) {
                throw new WarehouseProductException(
                        "Ikke nok på lager til flytning. Lager ID: " + request.getFromWarehouseId()
                );
            }

            // Find mål-lager
            Warehouse targetWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                    .orElseThrow(() -> new WareHouseNotFoundException("id: "+ request.getToWarehouseId()));

            // Find produkt på mål-lager ellers opret det
            WarehouseProduct target = warehouseProductRepository
                    .findByWarehouseIdAndProductId(request.getToWarehouseId(), request.getProductId())
                    .orElseGet(() -> {
                        WarehouseProduct wp = new WarehouseProduct();
                        wp.setWarehouse(targetWarehouse);
                        wp.setProduct(source.getProduct());
                        wp.setId(new WarehouseProductId(request.getToWarehouseId(), request.getProductId()));
                        wp.setQuantity(0);
                        return wp;
                    });

            // Flyt antal
            source.setQuantity(source.getQuantity() - request.getAmount());
            target.setQuantity(target.getQuantity() + request.getAmount());

            // Save
            warehouseProductRepository.save(source);
            warehouseProductRepository.save(target);

            // Log
            logService.createLogFromUser(user,
                    "Har flyttet " + request.getAmount() +
                            " stk. af produkt '" + source.getProduct().getName() +
                            "' | Fra lager ID: " + request.getFromWarehouseId() +
                            " til lager ID: " + request.getToWarehouseId()
            );

            return request;

        } catch (LogException e){
            throw new WarehouseProductException("Fejl oprettelse af logs " + e.getMessage(), e);
        } catch (WarehouseProductNotFoundException | WarehouseProductFetchException | WareHouseNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new WarehouseProductException("Fejl under flytning af produkt: " + e.getMessage(), e);
        }
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
