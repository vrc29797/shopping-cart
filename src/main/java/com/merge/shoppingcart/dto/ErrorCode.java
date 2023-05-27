package com.merge.shoppingcart.model;

public enum ErrorCode {


    DEFAULT("There was some error. Please try again"),
    PRODUCT_NOT_FOUND("Product with given Id is not found"),
    USER_NOT_FOUND("User with given email is not found"),
    USER_NOT_ACTIVE("User with given email is not active"),
    EMAIL_ALREADY_TAKEN("User already exists with this Email"),
    PRODUCT_NOT_ACTIVE("Product with given Id is not active"),
    PRODUCT_OUT_OF_STOCK("Product with given Id is out of Stock"),
    CART_NOT_FOUND("No Cart found for User, Please add an item to create cart");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
