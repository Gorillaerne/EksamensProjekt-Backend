package gustavo.com.eksamenprojektbackend.Warehouse.Repository;


import gustavo.com.eksamenprojektbackend.Warehouse.Model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IWarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Optional<Warehouse> findById(int id);
}
