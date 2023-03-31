package com.cwz.electronic.store.exception;

import com.cwz.electronic.store.dtos.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // to handle resource not found exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);

    }

    // to handle MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequest ex) {

        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);

    }

}
