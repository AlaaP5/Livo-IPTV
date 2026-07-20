package com.rand.ishowApi.exception.handler;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.rand.ishowApi.messages.resolver.MessageResolver.resolve;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger exceptionLogger = LoggerFactory.getLogger("exceptionLogger");

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception)
    {
        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(exception.getCode(), exception.getArgs()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage =resolve(error.getDefaultMessage(), null);
                    errors.add(errorMessage);
                });

        exceptionLogger.error("MethodArgumentNotValidException occurred: {} | errors: {}", exception.getMessage(), errors, exception);

        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(String.join("\n", errors), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handle(Exception exception)
    {
        exceptionLogger.error("Unhandled exception occurred: {}", exception.getMessage());

        return ResponseEntity
                .ok()
                .body(ApiResponse.failure(/*ApiResponseCode.GENERAL_ERROR*/ exception.getMessage(), null));
    }

}

