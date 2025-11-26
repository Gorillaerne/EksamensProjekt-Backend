package gustavo.com.eksamenprojektbackend.Warehouse.Controller;

import gustavo.com.eksamenprojektbackend.DTO.WarehouseProductExchangeDTO;
import gustavo.com.eksamenprojektbackend.Warehouse.Service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warehousetransfer")
public class WarehouseProductexchangeController {

    private final WarehouseService warehouseService;

    public WarehouseProductexchangeController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<WarehouseProductExchangeDTO> flytProdukt(
            @RequestBody @Valid WarehouseProductExchangeDTO request) {
        return ResponseEntity.ok(warehouseService.moveProduct(request));
    }
}

