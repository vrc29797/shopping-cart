package com.merge.shoppingcart.exception;

import com.merge.shoppingcart.dto.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends RuntimeException {

  private ErrorCode errorCode;
  private HttpStatus httpStatus;

  public ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }
}
