package gustavo.com.eksamenprojektbackend.Product.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gustavo.com.eksamenprojektbackend.Warehouse.Model.WarehouseProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String picture;

    @Column(unique = true)
    private String SKU;

    @Column()
    private Double price;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties
    List<WarehouseProduct> warehouseProductList = new ArrayList<>();

    public Product(String name, String description, String picture, String SKU, Double price, List<WarehouseProduct> warehouseProductList) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.SKU = SKU;
        this.price = price;
        this.warehouseProductList = warehouseProductList;
    }
}
