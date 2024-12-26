package com.ra.projectmd05.model.dto.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataError<T> {
    private T message;
    private HttpStatus status;
    private int statusCode;
}
//DataError<String> error = new DataError<>();
//error.setMessage("Resource not found");
//error.setStatus(HttpStatus.NOT_FOUND);
//error.setStatusCode(HttpStatus.NOT_FOUND.value());