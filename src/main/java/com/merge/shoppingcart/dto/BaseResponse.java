package com.merge.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
  private boolean success;
  private T data;
  private String error;
  private ErrorCode errorCode;

  public BaseResponse(T data) {
    if (data instanceof ErrorCode) {
      this.errorCode = (ErrorCode) data;
      this.error = errorCode.getDescription();
    } else {
      this.success = true;
      this.data = data;
    }
  }
}
