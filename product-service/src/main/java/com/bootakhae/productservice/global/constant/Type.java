package com.bootakhae.productservice.global.constant;

public enum Type {
    CAPSULE("캡슐"),
    POWDER("분말"),
    DRINK("액상"),
    SPRAY("스프레이"),
    PILL("환");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
