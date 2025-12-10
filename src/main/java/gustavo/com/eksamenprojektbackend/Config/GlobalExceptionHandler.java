package gustavo.com.eksamenprojektbackend.Config;


import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(FileProcessingException.class)
//    @ResponseBody
//    public ResponseEntity<String> handleFileProcessing(FileProcessingException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ex.getMessage());
//    }

}
