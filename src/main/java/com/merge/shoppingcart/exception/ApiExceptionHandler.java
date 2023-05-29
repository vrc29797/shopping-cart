package com.merge.shoppingcart.exception;

import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.dto.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<BaseResponse<ErrorCode>> handleApiException(ApiException ex) {
    BaseResponse<ErrorCode> response = new BaseResponse<>(ex.getErrorCode());
    return new ResponseEntity<>(response, ex.getHttpStatus());
  }
}
