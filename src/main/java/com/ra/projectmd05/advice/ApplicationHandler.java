package com.ra.projectmd05.advice;


import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.exception.DeleteNotAllowedException;
import com.ra.projectmd05.exception.UnauthorizedAccessException;
import com.ra.projectmd05.model.dto.errors.DataError;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice  // Đánh dấu lớp là một bộ xử lý ngoại lệ cho các RestController
public class ApplicationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataError<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((errorField) -> {
            errors.put(errorField.getField(), errorField.getDefaultMessage());
        });
        return new DataError<>(errors, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }
    // Xử lý ngoại lệ khi dữ liệu đã tồn tại
    @ExceptionHandler(DataExistException.class) // Bắt ngoại lệ DataExistException
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Đặt trạng thái HTTP trả về là 400 Bad Request
    public DataError<String> handleErrorDataExist(DataExistException ex) {
//        Map<String, String> errors = new HashMap<>(); // Tạo một Map để lưu trữ lỗi
//        errors.put("error",ex.getMessage()); // Lưu tên trường và thông báo lỗi vào Map
//        // Trả về đối tượng DataError chứa lỗi và thông tin trạng thái HTTP
        return new DataError<>( ex.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }

    // Xử lý ngoại lệ khi không tìm thấy phần tử
    @ExceptionHandler(NoSuchElementException.class) // Bắt ngoại lệ NoSuchElementException
    @ResponseStatus(HttpStatus.NOT_FOUND) // Đặt trạng thái HTTP trả về là 404 Not Found
    public DataError<String> handleErrorNoSuchElement(NoSuchElementException ex) {
        // Trả về đối tượng DataError chứa thông báo lỗi và thông tin trạng thái HTTP
        return new DataError<>(ex.getMessage(), HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public DataError<String> handleErrorUnauthorizedAccess(UnauthorizedAccessException ex) {
        return new DataError<>(ex.getMessage(), HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(DeleteNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataError<String> handleDeleteNotAllowed(DeleteNotAllowedException ex) {
        return new DataError<>(ex.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataError<String> handleDeleteNotAllowed(IllegalArgumentException ex) {
        return new DataError<>(ex.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataError<String> handleDeleteNotAllowed(BadCredentialsException ex) {
        return new DataError<>(ex.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataError<String> handleDeleteNotAllowed(BadRequestException ex) {
        return new DataError<>(ex.getMessage(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }



}
//DataError<String> error = new DataError<>();
//error.setMessage("Resource not found");
//error.setStatus(HttpStatus.NOT_FOUND);
//error.setStatusCode(HttpStatus.NOT_FOUND.value());

