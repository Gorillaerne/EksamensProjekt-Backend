package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseProductExchangeService;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehousetransfer")
public class WarehouseProductexchangeController {

    private final WarehouseService warehouseService;

    private final WarehouseProductExchangeService warehouseProductExchangeService;

    public WarehouseProductexchangeController(WarehouseService warehouseService, WarehouseProductExchangeService warehouseProductExchangeService) {
        this.warehouseService = warehouseService;
        this.warehouseProductExchangeService = warehouseProductExchangeService;
    }

    @PostMapping("")
    public ResponseEntity<WarehouseProductExchangeDTO> flytProdukt(
            @RequestBody @Valid WarehouseProductExchangeDTO request) {
        return ResponseEntity.ok(warehouseService.moveProduct(request));
    }

    @GetMapping("/{warehouseId}/product/{productId}/quantity")
    public ResponseEntity<Integer> getProductQuantity(
            @PathVariable Integer warehouseId,
            @PathVariable Integer productId) {

        int qty = warehouseProductExchangeService.getProductQuantity(warehouseId, productId);
        return ResponseEntity.ok(qty);
    }

}

