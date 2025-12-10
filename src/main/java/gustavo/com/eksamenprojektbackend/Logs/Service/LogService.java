package gustavo.com.eksamenprojektbackend.Logs.Service;

import gustavo.com.eksamenprojektbackend.Logs.Model.Logs;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
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

    public List<Logs> getAll() {
        return logRepository.findAll();
    }

    public List<Logs> getLogsByUserID(int userID) {
        userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        return logRepository.findByUserId(userID);
        
    }

    public List<Logs> getLogsByProductID(int productID) {
        return logRepository.findByProductId(productID);
    }

    public Logs createLogFromUser(User user, String message) {

        Logs logs = new Logs();
        logs.setUser(user);
        logs.setAction(message);
        logs.setTimeStamp(LocalDateTime.now());

        return logRepository.save(logs);
    }

    public Logs createLogFromProduct(Product product, User user, String message) {

        Logs logs = new Logs();
        logs.setProduct(product);
        logs.setUser(user);
        logs.setAction(message);
        logs.setTimeStamp(LocalDateTime.now());

        Logs saved = logRepository.save(logs);

        return saved;

    }

}
