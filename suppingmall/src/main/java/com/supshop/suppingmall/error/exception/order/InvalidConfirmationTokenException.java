package com.supshop.suppingmall.error.exception.order;

import com.supshop.suppingmall.error.exception.BusinessException;

public class InvalidConfirmationTokenException extends BusinessException {

    public InvalidConfirmationTokenException() {
        super();
    }

    public InvalidConfirmationTokenException(String message) {
        super(message);
    }
}
