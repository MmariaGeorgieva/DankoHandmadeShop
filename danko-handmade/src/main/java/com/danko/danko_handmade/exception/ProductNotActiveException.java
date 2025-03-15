package com.danko.danko_handmade.exception;

public class ProductNotActiveException extends RuntimeException{

    public ProductNotActiveException() {
    }

    public ProductNotActiveException(String message) {
        super(message);
    }
}
