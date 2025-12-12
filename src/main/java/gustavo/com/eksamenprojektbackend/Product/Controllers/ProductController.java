package gustavo.com.eksamenprojektbackend.Product.Controllers;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductDTO;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductWarehouseDTO;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductWithWarehouseDTO;
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
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody ProductDTO product) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product, user));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/searchBar")
    public ResponseEntity<?> getAllProductsForSearchBar() {
        return new ResponseEntity<>(productService.getAllProductsForSearchBar(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<?> getAllProductsFor() {
        return new ResponseEntity<>(productService.getAllProductsDto(), HttpStatus.OK);
    }

    @PostMapping("/delivery")
    public ResponseEntity<?> registerDeliveryOfGoods(Authentication authentication, @RequestBody List<RegisterDeliveryDTO> deliveryDTOS){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(productService.registerDeliveryOfGoods(deliveryDTOS, user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(Authentication authentication, @PathVariable Integer id, @RequestBody ProductDTO product) {
        User user = (User) authentication.getPrincipal();
            Product updatedProduct = productService.updateProduct(id, product, user);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductWithWarehouseDTO> getProductWithWarehouseDTO (@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductWithWarehouseDTO(id));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id){
//
//
//    }



}