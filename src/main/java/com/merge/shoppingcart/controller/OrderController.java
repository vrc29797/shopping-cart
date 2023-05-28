package com.merge.shoppingcart.controller;

import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.dto.Cart;
import com.merge.shoppingcart.dto.CartRequest;
import com.merge.shoppingcart.service.OrderService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

  @Autowired OrderService orderService;

  @PostMapping("/addToCart")
  public ResponseEntity<BaseResponse<Cart>> addToCart(
      @Valid @RequestBody CartRequest cartRequest, Authentication authentication) {
    log.info("addToCart {} user {}", cartRequest, authentication);
    Cart cart =
        orderService.addToCart(
            cartRequest.getId(), cartRequest.getQuantity(), authentication.getName());
    return ResponseEntity.ok(new BaseResponse<>(cart));
  }

  @PostMapping("/removeFromCart")
  public ResponseEntity<BaseResponse<Cart>> removeFromCart(
      @Valid @RequestBody CartRequest cartRequest, Authentication authentication) {
    log.info("removeFromCart {} user {}", cartRequest, authentication);
    Cart cart =
        orderService.removeFromCart(
            cartRequest.getId(), cartRequest.getQuantity(), authentication.getName());
    return ResponseEntity.ok(new BaseResponse<>(cart));
  }

  @GetMapping("/getCart")
  public ResponseEntity<BaseResponse<Cart>> getCart(Authentication authentication) {
    log.info("getCart {}", authentication.getName());

    Cart cart = orderService.getCart(authentication.getName());
    return ResponseEntity.ok(new BaseResponse<>(cart));
  }
}
