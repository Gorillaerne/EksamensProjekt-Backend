package gustavo.com.eksamenprojektbackend.Product.Repository;

import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepostiory extends JpaRepository<Product,Integer> {
}
