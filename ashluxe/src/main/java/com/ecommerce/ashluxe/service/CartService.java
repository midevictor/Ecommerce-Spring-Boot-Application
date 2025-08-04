package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.payload.CartDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCartByUserId(String emailId, Long cartId);
    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);
    String deleteCartItem(Long cartId, Long productId);
}
