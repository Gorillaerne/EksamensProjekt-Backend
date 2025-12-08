package gustavo.com.eksamenprojektbackend.Warehouse.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

@Entity
public class WarehouseProduct {

    @EmbeddedId
    private WarehouseProductId id;

    @ManyToOne
    @JsonBackReference
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JsonBackReference
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

    public WarehouseProductId getId() {
        return id;
    }

    public void setId(WarehouseProductId id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
