package gustavo.com.eksamenprojektbackend.Models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WarehouseProductId implements Serializable {

    private Long warehouseId;
    private Long productId;

    public WarehouseProductId() {}

    public WarehouseProductId(Long warehouseId, Long productId) {
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
