package com.williameliasson.car_auction_backend.exceptions;

public class IllegalBidException extends RuntimeException{
    private String errorCode;
    public IllegalBidException(String errorCode, String string){
        super(string);
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }
}
