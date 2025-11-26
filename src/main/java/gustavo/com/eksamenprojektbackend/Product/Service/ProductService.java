package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Models.User;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepostiory;
    private final LogService logService;

    public ProductService(IProductRepository productRepostiory, LogService logService) {
        this.productRepostiory = productRepostiory;
        this.logService = logService;
    }

    public Product createProduct(Product product, User user) {
        try {
            Product createdProduct = productRepostiory.save(product);
            logService.createLogFromProduct(createdProduct, user, "User: " + user.getUsername() +" | Har oprettet et nyt produkt: " + createdProduct.getName());
            return createdProduct;

        }catch (Exception e){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,"Noget gik galt under oprettelse af produkt");
        }
    }

    public List<Product> getAllProducts(){
        return productRepostiory.findAll();
    }
}
