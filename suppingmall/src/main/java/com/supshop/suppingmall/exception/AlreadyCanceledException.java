package com.supshop.suppingmall.exception;

public class AlreadyCanceledException extends RuntimeException{

    public AlreadyCanceledException(String message) {
        super(message);
    }
}