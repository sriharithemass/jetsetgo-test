package com.training.exception;

public class ResourceNotFoundException extends RuntimeException{

    String resource;
    String field;
    Long fieldId;

    public ResourceNotFoundException(){

    }

    public ResourceNotFoundException(String resource, String field,Long fieldId){
        super(String.format("%s not found with %s: %s",resource,field,fieldId));
        this.resource=resource;
        this.field=field;
        this.fieldId=fieldId;
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}