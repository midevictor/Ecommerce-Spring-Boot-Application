package com.ecommerce.ashluxe.exceptions;


import com.ecommerce.ashluxe.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            //we can also add custom error messages here
            response.put(fieldName, message);
        });

        return  new ResponseEntity<Map<String, String>>(response, org.springframework.http.HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
      return new ResponseEntity<>(apiResponse, org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public  ResponseEntity<ApiResponse> myAPIException(APIException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public  ResponseEntity<ApiResponse> APIException(APIException e) {
//        String message = e.getMessage();
//        ApiResponse apiResponse = new ApiResponse(message, false);
//        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//    }

}
