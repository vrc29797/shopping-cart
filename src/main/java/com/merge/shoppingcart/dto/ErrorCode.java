package com.merge.shoppingcart.dto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ErrorCode {
  DEFAULT("There was some error. Please try again"),
  PRODUCT_NOT_FOUND("Product with given Id is not found"),
  USER_NOT_FOUND("User with given email is not found"),
  CANNOT_SUSPEND_ADMIN("User with admin role cannot be suspended"),
  USER_PASS_INVALID("Email or Password in Invalid"),
  USER_SUSPENDED("User with given email is suspended"),
  EMAIL_ALREADY_TAKEN("User already exists with this Email"),
  INVALID_ROLE("Invalid role, Role can only be ROLE_ADMIN or ROLE_USER"),
  PRODUCT_NOT_IN_CART("Product with given Id is not in cart"),
  PRODUCT_OUT_OF_STOCK("Product with given Id is out of Stock"),
  CART_NOT_FOUND("No Cart found for User, Please add an item to create cart"),
  TOO_BIG_QUANTITY("Please check quantity, More quantity than actual present"),
  TOO_LOW_QUANTITY("Quantity cannot be less than 1");

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
      log.warn("Could not parse ErrorCode ", e);
    }
    return DEFAULT;
  }
}
