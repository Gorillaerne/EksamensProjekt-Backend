package gustavo.com.eksamenprojektbackend.Logs.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogCreationException;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserNotFoundException;
import gustavo.com.eksamenprojektbackend.Logs.Model.Logs;
import gustavo.com.eksamenprojektbackend.Logs.Repository.ILogRepository;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.Product.Model.Product;
import gustavo.com.eksamenprojektbackend.Product.Repository.IProductRepository;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    
    private final ILogRepository logRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public LogService(ILogRepository logRepository, IUserRepository userRepository, IProductRepository productRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;

    }

    public List<Logs> getAll() {
        return logRepository.findAll();
    }

    public List<Logs> getLogsByUserID(int userID) {


        userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("Bruger med id: " + userID + " kunne ikke findes"));

        List<Logs> logs = logRepository.findByUserId(userID);

        return logs;
    }

    public List<Logs> getLogsByProductID(int productID) {

        productRepository.findById(productID)
                .orElseThrow(() -> new ProductNotFoundException("Produkt med ID " + productID + " kunne ikke findes"));

        List<Logs> logs = logRepository.findByProductId(productID);

        return logs;
    }

    public Logs createLogFromUser(User user, String message) {

        if (user == null) {
            throw new LogCreationException("User kan ikke være null");
        }

        if (message == null || message.isBlank()) {
            throw new LogCreationException("Log-besked kan ikke være tom");
        }

        Logs logs = new Logs();
        logs.setUser(user);
        logs.setAction(message);
        logs.setTimeStamp(LocalDateTime.now());

        try {
            return logRepository.save(logs);
        } catch (DataIntegrityViolationException e) {
            throw new LogCreationException("Fejl under oprettelse af log, indhold overholder ikke database standard: " + e.getMessage(), e);
        }
    }

    public Logs createLogFromProduct(Product product, User user, String message) {

        if (product == null) {
            throw new LogCreationException("Product kan ikke være null");
        }
        if (user == null) {
            throw new LogCreationException("User kan ikke være null");
        }
        if (message == null || message.isBlank()) {
            throw new LogCreationException("Log-besked kan ikke være tom");
        }

        Logs logs = new Logs();
        logs.setProduct(product);
        logs.setUser(user);
        logs.setAction(message);
        logs.setTimeStamp(LocalDateTime.now());

        try {
            return logRepository.save(logs);
        } catch (DataIntegrityViolationException e) {
            throw new LogCreationException(
                    "Fejl under oprettelse af log, indhold overholder ikke database standard: " + e.getMessage(), e
            );
        }
    }

}
