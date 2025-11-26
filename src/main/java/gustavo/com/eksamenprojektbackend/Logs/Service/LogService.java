package gustavo.com.eksamenprojektbackend.Logs.Service;

import gustavo.com.eksamenprojektbackend.Logs.DTO.LogDTO;
import gustavo.com.eksamenprojektbackend.Logs.Model.Log;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.Models.User;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    
    private final ILogRepository logRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public LogService(ILogRepository logRepository, IUserRepository userRepository, IProductRepository productRepostiory, IProductRepository productRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;

    }

    public List<Log> getAll() {
        return logRepository.findAll();
    }

    public List<Log> getLogsByUserID(int userID) {
        userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        return logRepository.findByUserId(userID);
        
    }

    public List<Log> getLogsByProductID(int productID) {
        return logRepository.findByProductId(productID);
    }

    public Log createLogFromUser(User user, String message) {

        Log log = new Log();
        log.setUser(user);
        log.setAction(message);
        log.setTimeStamp(LocalDateTime.now());

        return logRepository.save(log);
    }

    public Log createLogFromProduct(Product product, User user, String message) {

        Log log = new Log();
        log.setProduct(product);
        log.setUser(user);
        log.setAction(message);
        log.setTimeStamp(LocalDateTime.now());

        Log saved = logRepository.save(log);

        return saved;

    }
}
