package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseCreateDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;


    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("")
    public ResponseEntity<?> createWarehouse(Authentication authentication, @RequestBody WarehouseCreateDTO warehouse){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(warehouseService.createWarehouse(warehouse, user), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllWarehouses(Authentication authentication){
       return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<?> getAllWarehousesForDeliveryPage(){
        return new ResponseEntity<>(warehouseService.getAllWarehousesForDelivery(), HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(Authentication authentication, @PathVariable int id){
        return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouseFromId(Authentication authentication, @PathVariable int id, @RequestBody Warehouse warehouse) {
        User user = (User) authentication.getPrincipal();
        Warehouse updatedWarehouse = warehouseService.updateWarehouse(id, warehouse, user);
        return ResponseEntity.ok(updatedWarehouse);
    }

    @GetMapping("/lowQty")
    public ResponseEntity<List<WarehouseProductDTO>> getProductsLowOnQty(){
       return new ResponseEntity<>(warehouseService.getListOfProductsLowOnQty(), HttpStatus.OK);
    }

}
