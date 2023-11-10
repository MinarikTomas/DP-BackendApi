package sk.stuba.fei.uim.dp.attendanceapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyEnded;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyStarted;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotFound;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotStarted;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>("test", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            UserNotFound.class,
            ActivityNotFound.class,
            CardNotFound.class
    })
    public ResponseEntity<String> handleNotFound(UserNotFound ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class
    })
    public ResponseEntity<String> handleUserExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({
            ActivityAlreadyEnded.class,
            ActivityNotStarted.class,
            ActivityAlreadyStarted.class
    })
    public ResponseEntity<String> handleActivityExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CardAlreadyExists.class
    })
    public ResponseEntity<String> handleCardExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}