package com.ecommerce.ashluxe.controller;

import com.ecommerce.ashluxe.model.Cart;
import com.ecommerce.ashluxe.payload.CartDTO;
import com.ecommerce.ashluxe.repositories.CartRepository;
import com.ecommerce.ashluxe.service.CartService;
import com.ecommerce.ashluxe.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public  ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOList = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOList, HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartByUser(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCartByUserId(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long productId, @PathVariable String operation) {
        CartDTO updatedCartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(updatedCartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long cartId ,@PathVariable Long productId) {
        String status = String.valueOf(cartService.deleteCartItem(cartId, productId));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
