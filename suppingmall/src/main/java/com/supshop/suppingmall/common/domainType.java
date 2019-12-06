package com.supshop.suppingmall.common;

public enum domainType {

    PRODUCT("PRODUCT"),
    USER("USER"),
    BOARD("BOARD");

    private String title;

    domainType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
