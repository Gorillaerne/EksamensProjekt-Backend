package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Product.DTO.RegisterDeliveryDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepostiory;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final IWarehouseRepository warehouseRepository;

    private final IProductRepostiory productRepostiory;

    public ProductService(IWarehouseRepository warehouseRepository, IProductRepostiory productRepostiory) {
        this.warehouseRepository = warehouseRepository;
        this.productRepostiory = productRepostiory;
    }

    public Product createProduct(Product product) {
        try {
            return productRepostiory.save(product);
        }catch (Exception e){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,"Noget gik galt under oprettelse af produkt");
        }
    }

    public List<Product> getAllProducts(){
        return productRepostiory.findAll();
    }


    public List<RegisterDeliveryDTO> registerDeliveryOfGoods(List<RegisterDeliveryDTO> deliveryDTOS) {

        if(deliveryDTOS == null || deliveryDTOS.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingen varer i request");
        }


        for(RegisterDeliveryDTO dto : deliveryDTOS){

            Product product = productRepostiory.findById(dto.productId()).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produkter kunne ikke findes")
                    );

            Warehouse warehouse = warehouseRepository.findById(dto.warehouseId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varehus kunne ikke findes")
                    );


        }

    }


}
