package gustavo.com.eksamenprojektbackend.Product.DTO;

public record ProductDTO(int id,String name,String description,String picture,String SKU,double price , int quantity) {

    public ProductDTO(int id, String name, String description, String picture, String SKU, double price) {
        this(id, name, description, picture, SKU, price, 0);
    }
}
