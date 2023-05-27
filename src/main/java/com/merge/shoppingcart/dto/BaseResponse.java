package com.merge.shoppingcart.model;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private String error;

    public BaseResponse() {
    }

    public BaseResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public BaseResponse(String error) {
        this.success = false;
        this.error = error;
    }


}
