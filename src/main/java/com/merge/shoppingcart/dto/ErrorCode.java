package com.merge.shoppingcart.dto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ErrorCode {
  DEFAULT("There was some error. Please try again"),
  PRODUCT_NOT_FOUND("Product with given Id is not found"),
  USER_NOT_FOUND("User with given email is not found"),
  CANNOT_SUSPEND_ADMIN("User with admin role cannot be suspended"),
  USER_PASS_INVALID("Email or Password in Invalid"),
  USER_NOT_ACTIVE("User with given email is not active"),
  EMAIL_ALREADY_TAKEN("User already exists with this Email"),
  INVALID_ROLE("Invalid role, Role can only be ADMIN or USER"),
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

  public static ErrorCode safeValueOf(String errorCodeString) {
    try {
      return ErrorCode.valueOf(errorCodeString);
    } catch (Exception e) {
      log.warn("Could not parse ErrorCode");
    }
    return DEFAULT;
  }
}
