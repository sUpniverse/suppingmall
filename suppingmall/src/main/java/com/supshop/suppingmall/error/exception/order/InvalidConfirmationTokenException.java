package com.supshop.suppingmall.error.exception.order;

public class InvalidConfirmationTokenException extends RuntimeException {

    public InvalidConfirmationTokenException() {
        super();
    }

    public InvalidConfirmationTokenException(String message) {
        super(message);
    }
}
