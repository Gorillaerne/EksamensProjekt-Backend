package gustavo.com.eksamenprojektbackend.Config;

import gustavo.com.eksamenprojektbackend.Exceptions.DeliveryException.DeliveryRegistrationException;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogCreationException;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.LogExceptions.LogException;

import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductCreationException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductFetchException;
import gustavo.com.eksamenprojektbackend.Exceptions.ProductException.ProductNotFoundException;
import gustavo.com.eksamenprojektbackend.Exceptions.UserExceptions.*;


import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseExceptions.*;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductFetchException;
import gustavo.com.eksamenprojektbackend.Exceptions.WarehouseProductExceptions.WarehouseProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------ Log Exceptions ------------------
    @ExceptionHandler(LogException.class)
    @ResponseBody
    public ResponseEntity<String> handleLogException(LogException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Log-fejl: " + ex.getMessage());
    }

    @ExceptionHandler(LogNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleLogNotFoundException(LogNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Log ikke fundet: " + ex.getMessage());
    }

    @ExceptionHandler(LogCreationException.class)
    @ResponseBody
    public ResponseEntity<String> handleLogCreationException(LogCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under oprettelse af log: " + ex.getMessage());
    }

    // ------------------ User Exceptions ------------------
    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User- fejl: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Bruger ikke fundet: " + ex.getMessage());
    }

    @ExceptionHandler(UserCreationException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserCreationException(UserCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under oprettelse af bruger: " + ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Bruger findes allerede: " + ex.getMessage());
    }

    @ExceptionHandler(UserDeletionException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserDeletionException(UserDeletionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under sletning af bruger: " + ex.getMessage());
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseBody
    public ResponseEntity<String> handleUserLogin(LoginFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }




    // ------------------ Product Exceptions ------------------
    @ExceptionHandler(ProductException.class)
    @ResponseBody
    public ResponseEntity<String> handleProductException(ProductException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Produkt fejl:  " + ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Produkt ikke fundet: " + ex.getMessage());
    }

    @ExceptionHandler(ProductCreationException.class)
    @ResponseBody
    public ResponseEntity<String> handleProductCreationException(ProductCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under oprettelse af produkt: " + ex.getMessage());
    }
    @ExceptionHandler(ProductFetchException.class)
    @ResponseBody
    public ResponseEntity<String> handleProductFetchException(ProductFetchException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Indhentning af produkt fejlet: " + ex.getMessage());
    }

    // ------------------ Warehouse------------------

    @ExceptionHandler(WareHouseException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseException(WareHouseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Varehus fejl: " + ex.getMessage());
    }

    @ExceptionHandler(WareHouseNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseNotFoundException(WareHouseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Varehus ikke fundet: " + ex.getMessage());
    }

    @ExceptionHandler(WareHouseCreationException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseCreationException(WareHouseCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under oprretelse af varehus: " + ex.getMessage());
    }

    @ExceptionHandler(WareHouseFetchException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseFetchException(WareHouseFetchException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Fejl under indhentning af varehus: " + ex.getMessage());
    }

    @ExceptionHandler(WareHouseUpdateException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseUpdateException(WareHouseUpdateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl under opdatering af varehus: " + ex.getMessage());
    }


    // ------------------Delivery Exceptions ------------------

    @ExceptionHandler(DeliveryRegistrationException.class)
    @ResponseBody
    public ResponseEntity<String> handleDeliveryRegistration(DeliveryRegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fejl ved registrering af levering: " + ex.getMessage());
    }


    // ------------------ WarehouseProduct------------------
    @ExceptionHandler(WarehouseProductException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseProductException(WarehouseProductException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Warehouse error: " + ex.getMessage());
    }


    @ExceptionHandler(WarehouseProductFetchException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseProductFetchException(WarehouseProductFetchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Warehouse fetch error: " + ex.getMessage());
    }


    @ExceptionHandler(WarehouseProductNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleWarehouseProductNotFoundException(WarehouseProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Warehouse product not found: " + ex.getMessage());
    }

    // ------------------ Catch-All Exception ------------------
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleAllOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Noget gik galt: " + ex.getMessage());
    }


}
