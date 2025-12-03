package gustavo.com.eksamenprojektbackend.Product.DTO;

public record RegisterDeliveryDTO(Integer warehouseId, Integer productId, int quantity) {

    @Override
    public String toString() {
        return "RegisterDeliveryDTO{" +
                "warehouseId=" + warehouseId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }


}
