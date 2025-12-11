package gustavo.com.eksamenprojektbackend.Warehouse.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(length = 99999999)
    private String description;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnoreProperties
    List<WarehouseProduct> warehouseProductList = new ArrayList<>();

    // JPA kræver tom konstruktør
    public Warehouse() {
    }

    public Warehouse(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }
    }


