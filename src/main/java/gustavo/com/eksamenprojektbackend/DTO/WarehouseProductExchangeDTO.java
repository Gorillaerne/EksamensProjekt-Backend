package gustavo.com.eksamenprojektbackend.DTO;

public class WarehouseProductExchangeDTO {

    private int productId;
    private int fromWarehouseId;
    private int toWarehouseId;
    private int amount;

    public WarehouseProductExchangeDTO() {
    }

    public WarehouseProductExchangeDTO(int productId, int fromWarehouseId, int toWarehouseId, int amount) {
        this.productId = productId;
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.amount = amount;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public int getFromWarehouseId() {
        return fromWarehouseId;
    }

    public int getToWarehouseId() {
        return toWarehouseId;
    }

    public int getAmount() {
        return amount;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setFromWarehouseId(int fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public void setToWarehouseId(int toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
