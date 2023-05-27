package com.merge.shoppingcart.exception;

import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.dto.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<BaseResponse<ErrorCode>> handleApiException(ApiException ex) {
    BaseResponse<ErrorCode> response = new BaseResponse<>(ErrorCode.safeValueOf(ex.getMessage()));
    // can extend this to other cases as and when required for returning other HTTP Status.
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
