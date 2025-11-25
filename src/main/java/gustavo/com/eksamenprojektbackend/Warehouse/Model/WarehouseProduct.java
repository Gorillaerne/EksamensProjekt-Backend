package gustavo.com.eksamenprojektbackend.Warehouse.Model;

import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

@Entity
public class WarehouseProduct {

    @EmbeddedId
    private WarehouseProductId id;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column (nullable = false)
    private int quantity;

    public WarehouseProduct() {}

    public WarehouseProduct(Warehouse warehouse, Product product, int quantity) {
        this.warehouse = warehouse;
        this.product = product;
        this.id = new WarehouseProductId(
                warehouse.getId(),
                product.getId()
        );
        this.quantity = quantity;
    }

    // getters & setters
}
