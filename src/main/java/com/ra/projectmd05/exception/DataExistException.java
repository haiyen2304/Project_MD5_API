package com.ra.projectmd05.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataExistException extends Exception{
//    private String field;
    public DataExistException(String message){
        super(message);
//        this.field = field;
    }
}
