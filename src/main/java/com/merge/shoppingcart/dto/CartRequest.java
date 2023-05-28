package com.merge.shoppingcart.dto;

import java.util.UUID;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
  UUID id;
  int quantity;
}
