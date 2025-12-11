package gustavo.com.eksamenprojektbackend.Product.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.DeliveryException.DeliveryRegistrationException;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductCreationException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductFetchException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions.WareHouseNotFoundException;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.Product.DTO.*;
import gustavo.com.eksamenprojektbackend.Product.DTO.ProductDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProductId;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseProductRepository;
import gustavo.com.eksamenprojektbackend.Warehouse.Repository.IWarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final IProductRepository productRepository;
    private final LogService logService;
    private final IWarehouseProductRepository warehouseProductRepository;
    private final IWarehouseRepository warehouseRepository;


    public ProductService(IProductRepository productRepository, LogService logService, IWarehouseProductRepository warehouseProductRepository, IWarehouseRepository warehouseRepository) {
        this.productRepository = productRepository;
        this.logService = logService;
        this.warehouseProductRepository = warehouseProductRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, User user) {
        if (productDTO == null) throw new ProductCreationException("Product data må ikke være null");
        if (user == null) throw new ProductCreationException("User må ikke være null");

        try {
            Product createdProduct = productRepository.save(
                    new Product(
                            productDTO.name(),
                            productDTO.description(),
                            productDTO.picture(),
                            productDTO.SKU(),
                            productDTO.price(),
                            new ArrayList<>()
                    )
            );


            logService.createLogFromProduct(createdProduct, user, "Oprettet nyt produkt: " + createdProduct.getName());

            List<WarehouseProduct> warehouseProducts = warehouseRepository.findAll()
                    .stream()
                    .map(wp -> new WarehouseProduct(wp, createdProduct, 0))
                    .toList();

            warehouseProductRepository.saveAll(warehouseProducts);


            return new ProductDTO(
                    createdProduct.getId(),
                    createdProduct.getName(),
                    createdProduct.getDescription(),
                    createdProduct.getPicture(),
                    createdProduct.getSKU(),
                    createdProduct.getPrice()
            );

        } catch (LogException e) {
            throw new ProductCreationException("Fejl under oprettelse af logs til produktet", e);
        } catch (Exception e) {
            throw new ProductCreationException("Noget gik galt under oprettelse af produkt", e);
        }
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<SearchBarProductDTO> getAllProductsForSearchBar(){
        List<Product> productList = productRepository.findAll();

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

    @Transactional
    public ResponseDeliveryDTO registerDeliveryOfGoods(List<RegisterDeliveryDTO> deliveryDTOS, User user) {

        if (deliveryDTOS == null || deliveryDTOS.isEmpty()) {
            throw new DeliveryRegistrationException("Ingen varer i request");
        }


        for(RegisterDeliveryDTO dto : deliveryDTOS){

            if (dto.quantity() <= 0) {
                throw new DeliveryRegistrationException("Ugyldigt antal: " + dto.quantity());
            }

            WarehouseProductId warehouseProductId = new WarehouseProductId(dto.warehouseId(), dto.productId());


            WarehouseProduct warehouseProduct = warehouseProductRepository.findById(warehouseProductId)
                    .orElseGet(() -> {
                        WarehouseProduct wp = new WarehouseProduct();
                        wp.setId(warehouseProductId);
                        wp.setQuantity(0);
                        wp.setWarehouse(warehouseRepository.findById(dto.warehouseId())
                                .orElseThrow(() -> new WareHouseNotFoundException("Varehus med id : "+ dto.warehouseId() + " Ikke fundet")));
                        wp.setProduct(productRepository.findById(dto.productId())
                                .orElseThrow(() -> new ProductNotFoundException("Produkt med id : "+  dto.productId() + " Ikke fundet")));
                        return wp;
                    });

            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + dto.quantity());

            warehouseProductRepository.save(warehouseProduct);

            logService.createLogFromUser(user,
                    "Opdateret warehouse: " + warehouseProduct.getWarehouse().getName() +
                            " | Med produkt: " + warehouseProduct.getProduct().getName() +
                            " | Antal: " + warehouseProduct.getQuantity());
        }

        return new ResponseDeliveryDTO("Levering registreret", deliveryDTOS.size());
    }

    public Product updateProduct(Integer id, ProductDTO productRequest, User user) {

        if (productRequest == null) {
            throw new ProductCreationException("Ingen data modtaget til opdatering");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Kunne ikke finde produkt med id: " + id));

        StringBuilder changes = new StringBuilder();

        String oldName = product.getName();
        String oldDescription = product.getDescription();
        Double oldPrice = product.getPrice();
        String oldPicture = product.getPicture();
        String oldSKU = product.getSKU();

        if (productRequest.name() != null && !productRequest.name().equals(oldName)) {
            product.setName(productRequest.name());
            changes.append("navn ændret fra ").append(oldName).append(" til ").append(productRequest.name()).append(". ");
        }

        if (productRequest.description() != null && !productRequest.description().equals(oldDescription)) {
            product.setDescription(productRequest.description());
            changes.append("beskrivelse ændret fra ").append(oldDescription).append(" til ").append(productRequest.description()).append(". ");
        }

        if (productRequest.price() != null && !productRequest.price().equals(oldPrice)) {
            product.setPrice(productRequest.price());
            changes.append("pris ændret fra ").append(oldPrice).append(" til ").append(productRequest.price()).append(". ");
        }

        if (productRequest.picture() != null && !productRequest.picture().equals(oldPicture)) {
            product.setPicture(productRequest.picture());
            changes.append("billede er ændret");
        }

        if (productRequest.SKU() != null && !productRequest.SKU().equals(oldSKU)) {
            product.setSKU(productRequest.SKU());
            changes.append("SKU ændret fra ").append(oldSKU).append(" til ").append(productRequest.SKU()).append(". ");
        }

        Product productResponse = productRepository.save(product);

        if (!changes.isEmpty()) {
            logService.createLogFromProduct(productResponse, user, "Ændringer for produkt: " + changes);
        }

        return productResponse;
    }


    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produkt med id: " + id + " Kunne ikke findes"));
    }

    public List<ProductDTO> getAllProductsDto() {
        try {
            return productRepository.findAll().stream().map(product -> {
                int quantity = product.getWarehouseProductList().stream()
                        .mapToInt(WarehouseProduct::getQuantity)
                        .sum();
                return new ProductDTO(product.getId(), product.getName(), product.getDescription(),product.getPicture(),product.getSKU(),product.getPrice(),quantity);
            }).toList();
        } catch (Exception e) {
            throw new ProductFetchException("Fejl under hentning af produkter", e);
        }
    }

    public ProductWithWarehouseDTO getProductWithWarehouseDTO(Integer id) {
        Product product = getProductById(id);

        List<ProductWarehouseDTO> productWarehouseDTOS = product.getWarehouseProductList()
                .stream()
                .map(pw -> {
                    String warehouseName = pw.getWarehouse() != null ? pw.getWarehouse().getName() : "Ukendt";
                    return new ProductWarehouseDTO(pw.getId(), pw.getQuantity(), warehouseName);
                })
                .toList();

        return new ProductWithWarehouseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPicture(),
                product.getSKU(),
                product.getPrice(),
                productWarehouseDTOS
        );
    }
}
