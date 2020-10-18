package com.supshop.suppingmall.error.exception.order;

public class InvalidConfirmationToken extends RuntimeException {

    public InvalidConfirmationToken() {
        super();
    }

    public InvalidConfirmationToken(String message) {
        super(message);
    }
}
