package gustavo.com.eksamenprojektbackend.Logs.Repository;

import gustavo.com.eksamenprojektbackend.Logs.Model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILogRepository extends JpaRepository<Logs, Integer> {

    List<Logs> findByUserId(int userId);

    List<Logs> findByProductId(int productId);
}
