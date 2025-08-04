package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.exceptions.APIException;
import com.ecommerce.ashluxe.exceptions.ResourceNotFoundException;
import com.ecommerce.ashluxe.model.Cart;
import com.ecommerce.ashluxe.model.CartItems;
import com.ecommerce.ashluxe.model.Product;
import com.ecommerce.ashluxe.payload.CartDTO;
import com.ecommerce.ashluxe.payload.ProductDTO;
import com.ecommerce.ashluxe.repositories.CartItemsRepository;
import com.ecommerce.ashluxe.repositories.CartRepository;
import com.ecommerce.ashluxe.repositories.ProductRepository;
import com.ecommerce.ashluxe.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CartItemsRepository cartItemsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find an existing cart or create one
        Cart cart = createCart();
        //Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        //Perform Validations (if product exist, quantity)
        CartItems cartItems = cartItemsRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if(cartItems != null){
            throw new APIException("Product" + product.getProductName() + "already exist");
        }
        if(product.getQuantity() == 0){
            throw new APIException( product.getProductName() + "is not available");
        }
        if(product.getQuantity() < quantity){
            throw new APIException( "Please make an order of the " + product.getProductName() + "less than or equal to" + product.getQuantity() + ".");
        }

        //Create Cart Item
        CartItems newCartItems = new CartItems();
        newCartItems.setProduct(product);
        newCartItems.setCart(cart);
        newCartItems.setQuantity(quantity);
        newCartItems.setDiscount(product.getDiscount());
        newCartItems.setProductPrice(product.getSpecialPrice());
        //Save Cart Item
        cartItemsRepository.save(newCartItems);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        // Return updated CART
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItems> cartItemsList =  cart.getCartItems();

        Stream<ProductDTO> productStream = cartItemsList.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new APIException("No cart exist");
        }
        List<CartDTO> cartDTOs = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<CartItems> cartItemsList = cart.getCartItems();
                    List<ProductDTO> products = cartItemsList.stream()
                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                            .toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                }).toList();
        return cartDTOs;
    }

    @Override
    public CartDTO getCartByUserId(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<CartItems> cartItemsList = cart.getCartItems();
        List<ProductDTO> products = cartItemsList.stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        // Find the cart for the logged-in user
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // Find the cart item by product ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if(product.getQuantity() == 0){
            throw new APIException( product.getProductName() + "is not available");
        }
        if(product.getQuantity() < quantity){
            throw new APIException( "Please make an order of the " + product.getProductName() + "less than or equal to" + product.getQuantity() + ".");
        }

        CartItems cartItems = cartItemsRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItems == null) {
            throw new APIException("Product with ID " + productId + " not found in the cart.");
        }
        int currentQuantity = cartItems.getQuantity() + quantity;
        if (currentQuantity < 0) {
            throw new APIException("Cannot reduce quantity below zero for product with ID " + productId);
        }
        if(currentQuantity == 0){
            deleteCartItem(cartId, productId);
        }else {

            // Update the quantity of the cart item
            cartItems.setProductPrice(product.getSpecialPrice());
            cartItems.setQuantity(cartItems.getQuantity() + quantity);
            cartItems.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItems.getProductPrice() * quantity));
//        cartItemsRepository.save(cartItems);
            cartRepository.save(cart);
        }
        CartItems updatedCartItem = cartItemsRepository.save(cartItems);
        if(updatedCartItem.getQuantity() == 0){
            cartItemsRepository.deleteById(updatedCartItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItems> cartItemsList = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItemsList.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;

    }

    @Override
    @Transactional
    public String deleteCartItem(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItems cartItems = cartItemsRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItems == null) {
            throw new APIException("Product with ID " + productId + " not found in the cart.");
        }
        // Update the total price of the cart
        cart.setTotalPrice(cart.getTotalPrice() - (cartItems.getProductPrice() * cartItems.getQuantity()));
        // Remove the cart item from the cart
        cartItemsRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        return  "Product with ID " + productId + "from" + cartItems.getProduct().getProductName() + "is removed from the cart successfully.";
    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return  userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
