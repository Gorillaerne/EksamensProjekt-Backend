package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;


    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("")
    public ResponseEntity<?> createWarehouse(Authentication authentication, @RequestBody Warehouse warehouse){
        return new ResponseEntity<>(warehouseService.createWarehouse(warehouse), HttpStatus.CREATED);
    }

    @RequestMapping("")
    public ResponseEntity<?> getAllWarehouses(Authentication authentication){
       return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Warehouse>> getWarehouseById(Authentication authentication, @PathVariable int id){
        return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatus.FOUND);
    }

}
