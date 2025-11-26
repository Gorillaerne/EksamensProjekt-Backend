package gustavo.com.eksamenprojektbackend.DTO;

public class WarehouseProductExchangeDTO {



        private int productId;
        private int sourceWarehouseId;
        private int targetWarehouseId;
        private int antal;
        private String status;

        public WarehouseProductExchangeDTO(int productId, int sourceWarehouseId,
                                        int targetWarehouseId, int antal, String status) {
            this.productId = productId;
            this.sourceWarehouseId = sourceWarehouseId;
            this.targetWarehouseId = targetWarehouseId;
            this.antal = antal;
            this.status = status;
        }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSourceWarehouseId() {
        return sourceWarehouseId;
    }

    public void setSourceWarehouseId(int sourceWarehouseId) {
        this.sourceWarehouseId = sourceWarehouseId;
    }

    public int getTargetWarehouseId() {
        return targetWarehouseId;
    }

    public void setTargetWarehouseId(int targetWarehouseId) {
        this.targetWarehouseId = targetWarehouseId;
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// getters & setters
    }

