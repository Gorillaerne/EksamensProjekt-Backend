package gustavo.com.eksamenprojektbackend.User.Service;

import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserAlreadyExistsException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserCreationException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserDeletionException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.UserNotFoundException;
import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;
import gustavo.com.eksamenprojektbackend.User.DTO.UserRoleDTO;
import gustavo.com.eksamenprojektbackend.User.Model.User;
import gustavo.com.eksamenprojektbackend.User.DTO.UserDTO;
import gustavo.com.eksamenprojektbackend.User.Repository.IUserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder, LogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Brugernavn ikke eksisterer ikke"));
    }

    public User createUser(UserDTO dto, User user) {

        if (dto == null) {
            throw new UserCreationException("Ingen brugerdata modtaget");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setEmail(dto.getEmail());
        newUser.setRole(dto.getRole());


        try {
            User savedUser = userRepository.save(newUser);
            logService.createLogFromUser(user, "Har oprettet en ny bruger: " + newUser.getUsername());
            return savedUser;

        } catch (DataIntegrityViolationException e) {
            throw new UserCreationException(e.getMessage(), e);
        }

    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserRoleDTO getActiveUser(User user) {
        return new UserRoleDTO(user.getRole());
    }

    public void deleteUser(int id, User user) {
        User selectedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id: " + id));

        try {
            logService.createLogFromUser(user, "Har slettet en bruger: " + selectedUser);
            userRepository.delete(selectedUser);

        } catch (DataIntegrityViolationException e) {
            throw new UserDeletionException(e.getMessage(), e);
        }


    }
}
