package gustavo.com.eksamenprojektbackend.Product.Model;

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
    List<WarehouseProduct> warehouseProductList = new ArrayList<>();

}
