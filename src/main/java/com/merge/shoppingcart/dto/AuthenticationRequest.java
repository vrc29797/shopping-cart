package com.merge.shoppingcart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
  @NotNull
  @Size(min = 2, max = 50)
  private String email;

  @NotNull
  @Size(min = 2, max = 20)
  private String password;

  @NotNull
  @Size(min = 2, max = 20)
  private String role;
}
