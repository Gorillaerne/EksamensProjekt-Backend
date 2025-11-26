package gustavo.com.eksamenprojektbackend.Warehouse.Model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WarehouseProductId implements Serializable {

    private int warehouseId;
    private int productId;

    public WarehouseProductId() {}

    public WarehouseProductId(int warehouseId, int productId) {
        this.warehouseId = warehouseId;
        this.productId = productId;
    }

    // getters & setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseProductId)) return false;
        WarehouseProductId that = (WarehouseProductId) o;
        return Objects.equals(warehouseId, that.warehouseId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, productId);
    }
}
