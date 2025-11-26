package gustavo.com.eksamenprojektbackend.Product.Controllers;
import gustavo.com.eksamenprojektbackend.Models.User;
import gustavo.com.eksamenprojektbackend.Product.DTO.RegisterDeliveryDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct( @RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> registerDeliveryOfGoods(@RequestBody List<RegisterDeliveryDTO> deliveryDTOS){
        productService.registerDeliveryOfGoods(deliveryDTOS);
        return new ResponseEntity<>("Du har registreret varemodetagelsen", HttpStatus.CREATED);
    }


}
