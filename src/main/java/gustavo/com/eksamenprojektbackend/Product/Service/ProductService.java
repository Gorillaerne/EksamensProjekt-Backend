package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepostiory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class ProductService {

    private final IProductRepostiory productRepostiory;

    public ProductService(IProductRepostiory productRepostiory) {
        this.productRepostiory = productRepostiory;
    }

    public Product createProduct(Product product) {
        try {
            return productRepostiory.save(product);
        }catch (Exception e){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,"Noget gik galt under oprettelse af produkt");
        }
    }
}
