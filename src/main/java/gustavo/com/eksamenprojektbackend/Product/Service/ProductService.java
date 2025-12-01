package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Product.DTO.RegisterDeliveryDTO;
import gustavo.com.eksamenprojektbackend.Product.DTO.ResponseDeliveryDTO;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final IProductRepository productRepostiory;
    private final LogService logService;
    private final IWarehouseProductRepository warehouseProductRepository;
    private final IWarehouseRepository warehouseRepository;


    public ProductService(IProductRepository productRepostiory, LogService logService, IWarehouseProductRepository warehouseProductRepository, IWarehouseRepository warehouseRepository) {
        this.productRepostiory = productRepostiory;
        this.logService = logService;
        this.warehouseProductRepository = warehouseProductRepository;
        this.warehouseRepository = warehouseRepository;
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


    public ResponseDeliveryDTO registerDeliveryOfGoods(List<RegisterDeliveryDTO> deliveryDTOS) {
        if(deliveryDTOS == null || deliveryDTOS.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingen varer i request");
        }
        for(RegisterDeliveryDTO dto : deliveryDTOS){
            WarehouseProductId warehouseProductId = new WarehouseProductId(dto.warehouseId(), dto.productId());
            WarehouseProduct warehouseProduct;

            if(warehouseProductRepository.existsById(warehouseProductId)) {
                warehouseProduct = warehouseProductRepository.findById(warehouseProductId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ikke findes")
                );
                warehouseProduct.setQuantity(warehouseProduct.getQuantity() + dto.quantity());

            } else {
                warehouseProduct = new WarehouseProduct();
                warehouseProduct.setQuantity(dto.quantity());
                warehouseProduct.setWarehouse(warehouseRepository.findById(dto.warehouseId()).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kunne ikke opdatere varehus id")
                ));
                warehouseProduct.setProduct(productRepostiory.findById(dto.productId()).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kunne ikke opdatere produkt id")
                        ));
            }
            warehouseProductRepository.save(warehouseProduct);
        }
        return new ResponseDeliveryDTO("Levering registreret", deliveryDTOS.size());
    }

    public Product updateProduct(Integer id, Product productRequest){
        Optional<Product> optionalProduct = productRepostiory.findById(id);

        if(optionalProduct.isEmpty()){
            throw new RuntimeException("Produktet med dette id kunne ikke findes " + id);
        }

        Product product = optionalProduct.get();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setPicture(productRequest.getPicture());
        product.setSKU(productRequest.getSKU());

        Product productResponse = productRepostiory.save(product);
        return productResponse;
    }


}
