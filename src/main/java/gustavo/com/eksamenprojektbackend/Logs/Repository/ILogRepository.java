package gustavo.com.eksamenprojektbackend.Logs.Repository;

import gustavo.com.eksamenprojektbackend.Logs.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILogRepository extends JpaRepository<Log, Integer> {

    List<Log> findByUserId(int userId);

    List<Log> findByProductId(int productId);
}
