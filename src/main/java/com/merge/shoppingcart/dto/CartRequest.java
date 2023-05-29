package com.merge.shoppingcart.dto;

import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
  @NotNull UUID id;

  @NotNull
  @Min(1)
  @Max(100000)
  int quantity;
}
