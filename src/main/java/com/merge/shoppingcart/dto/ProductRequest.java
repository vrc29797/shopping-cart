package com.merge.shoppingcart.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class ProductRequest {

  @NotNull
  @Size(min = 2, max = 50)
  String name;

  @NotNull
  @Size(min = 2, max = 20)
  String category;

  @NotNull
  @Size(min = 2, max = 200)
  String description;

  @NotNull boolean active;

  @NotNull
  @Min(1)
  @Max(100000)
  int stock;

  @NotNull
  @Min(1)
  @Max(100000)
  int price;
}
