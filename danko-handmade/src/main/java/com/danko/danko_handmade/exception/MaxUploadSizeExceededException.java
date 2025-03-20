package com.danko.danko_handmade.exception;

public class MaxUploadSizeExceededException extends RuntimeException{

    public MaxUploadSizeExceededException() {
    }

    public MaxUploadSizeExceededException(String message) {
        super(message);
    }
}
