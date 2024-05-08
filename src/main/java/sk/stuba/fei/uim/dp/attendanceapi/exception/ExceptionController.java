package sk.stuba.fei.uim.dp.attendanceapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyEndedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityAlreadyStartedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.activity.ActivityNotStartedException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.card.CardWithoutUserException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.GoogleLoginException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserAlreadyExistsException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.UserNotFoundException;
import sk.stuba.fei.uim.dp.attendanceapi.exception.user.WrongPasswordException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CardNotFoundException.class,
            UserNotFoundException.class,
            ActivityNotFoundException.class,
    })
    public ResponseEntity<String> handleNotFound(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            WrongPasswordException.class,
            GoogleLoginException.class
    })
    public ResponseEntity<String> handleUserExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({
            ActivityAlreadyEndedException.class,
            ActivityNotStartedException.class,
            ActivityAlreadyStartedException.class
    })
    public ResponseEntity<String> handleActivityExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CardAlreadyExistsException.class,
            CardWithoutUserException.class
    })
    public ResponseEntity<String> handleCardExceptions(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
