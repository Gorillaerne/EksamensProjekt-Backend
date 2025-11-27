package gustavo.com.eksamenprojektbackend.Logs.Controller;

import gustavo.com.eksamenprojektbackend.Logs.Service.LogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllLogs() {
        return new ResponseEntity<>(logService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getLogsByUserID(@PathVariable int id) {
        return new ResponseEntity<>(logService.getLogsByUserID(id), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getLogsByProductID(@PathVariable int id) {
        return new ResponseEntity<>(logService.getLogsByProductID(id), HttpStatus.OK);
    }

//    @PostMapping("/user/{id}/log")
//    public ResponseEntity<LogDTO> createLogsFromUser(@PathVariable int id, @RequestBody String actionMessage) {
//
//        var result = logService.createLogFromUser(id, actionMessage);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/product/{id}/log")
//    public ResponseEntity<LogDTO> createLogsFromProduct(@PathVariable int productID, @RequestParam int userID , @RequestBody String actionMessage) {
//
//        var result = logService.createLogFromProduct(productID, userID, actionMessage);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
}
