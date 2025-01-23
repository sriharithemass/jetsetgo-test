package com.training.exception;

public class APIException extends RuntimeException{
    public APIException(){

    }

    public APIException(String message){
        super(message);
    }
}
