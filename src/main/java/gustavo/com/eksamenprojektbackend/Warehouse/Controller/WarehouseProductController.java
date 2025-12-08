package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import gustavo.com.eksamenprojektbackend.Product.DTO.ProductWarehouseDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.PatchWarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.DTO.WarehouseProductDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseProductExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/warehouseproducts")
public class WarehouseProductController {

    private final WarehouseProductExchangeService warehouseProductExchangeService;

    public WarehouseProductController(WarehouseProductExchangeService warehouseProductExchangeService) {
        this.warehouseProductExchangeService = warehouseProductExchangeService;
    }

    @PatchMapping("/")
    public ResponseEntity<?> patchWarehouseProduct(@RequestBody PatchWarehouseProductDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(warehouseProductExchangeService.patchWarehouseProduct( dto));
    }


}
