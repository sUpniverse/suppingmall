package com.supshop.suppingmall.error.exception.user;

import com.supshop.suppingmall.error.exception.BusinessException;

public class AlreadyAppliedSellerException extends BusinessException {

    public AlreadyAppliedSellerException() {
        super();
    }

    public AlreadyAppliedSellerException(String message) {
        super(message);
    }
}
