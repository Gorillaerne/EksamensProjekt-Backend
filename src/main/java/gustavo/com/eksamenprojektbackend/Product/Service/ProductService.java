package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.DTO.SearchBarProductDTO;
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
import org.springframework.http.HttpStatusCode;
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

    public List<SearchBarProductDTO> getAllProductsForSearchBar(){
        List<Product> productList   = productRepostiory.findAll();



        List<SearchBarProductDTO> dtoList = productList.stream()
                .map(product -> new SearchBarProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPicture(),
                        product.getSKU(),
                        product.getPrice()
                ))
                .toList();

        return dtoList;

    }

    public ResponseDeliveryDTO registerDeliveryOfGoods(List<RegisterDeliveryDTO> deliveryDTOS, User user) {

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
            logService.createLogFromUser(user, "User: " + user.getUsername() +
                    " | Har opdateret warehouse: " + warehouseProduct.getWarehouse().getName() +
                    " | Med produkt: " + warehouseProduct.getProduct().getName() +
                    " | Antal: " + warehouseProduct.getQuantity());

            warehouseProductRepository.save(warehouseProduct);
        }
        return new ResponseDeliveryDTO("Levering registreret", deliveryDTOS.size());
    }

    public Product updateProduct(Integer id, Product productRequest, User user) {
        Product product = productRepostiory.findById(id)
                .orElseThrow(()-> new RuntimeException("Produktet med dette id kunne ikke findes " + id));

        if (productRequest.getName() != null){
            product.setName(productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getPicture() != null){
            product.setPicture(productRequest.getPicture());
        }
        if (productRequest.getSKU() != null) {
            product.setSKU(productRequest.getSKU());
        }

        Product productResponse = productRepostiory.save(product);
        logService.createLogFromProduct(product,user, "User: " + user.getUsername() +" | Har ændret et produkt: " + productResponse.getName());
        return productResponse;
    }


    public Product getProductById(Integer id) {
        return productRepostiory.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produkt ikke fundet"));
    }
}
