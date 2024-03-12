package com.service.handler.ex;

public class CustomBadRequestException extends RuntimeException{
    public CustomBadRequestException(String msg){ super(msg);}
}
