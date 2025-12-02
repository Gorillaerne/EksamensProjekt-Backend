package gustavo.com.eksamenprojektbackend.Product.Controllers;
import gustavo.com.eksamenprojektbackend.User.Model.User;
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
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody Product product) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product, user));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/searchBar")
    public ResponseEntity<?> getAllProductsForSearchBar(){
        return new ResponseEntity<>(productService.getAllProductsForSearchBar(), HttpStatus.OK);
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> registerDeliveryOfGoods(@RequestBody List<RegisterDeliveryDTO> deliveryDTOS){
        return new ResponseEntity<>(productService.registerDeliveryOfGoods(deliveryDTOS), HttpStatus.CREATED);
    }


}
