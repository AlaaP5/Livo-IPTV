package rand.ishowStream.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rand.ishowStream.exception.model.CustomException;
import rand.ishowStream.response.ApiResponse;

import java.util.HashSet;
import java.util.Set;



@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger exceptionLogger = LoggerFactory.getLogger("exceptionLogger");

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception)
    {
        exceptionLogger.error("CustomException occurred: {}", exception.getMessage(), exception);

        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(exception.getCode(), exception.getArgs()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>>handle(Exception exception)
    {
        exceptionLogger.error("GeneralException occurred: {}", exception.getMessage(), exception);

        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(/*"SYS_GENERAL_EXCEPTION"*/exception.getMessage(), null));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage =("general error");
                    errors.add(errorMessage);
                });
        exceptionLogger.error("MethodArgumentNotValidException occurred: {}", exception.getMessage(), exception,errors);
        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(String.join("\n", errors), null));
    }
}
