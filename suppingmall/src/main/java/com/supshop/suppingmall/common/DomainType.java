package com.supshop.suppingmall.common;

public enum DomainType {

    PRODUCT("PRODUCT"),
    USER("USER"),
    BOARD("BOARD");

    private String title;

    DomainType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
