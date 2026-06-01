package com.snapBuy.project.controller;

import com.snapBuy.project.model.Cart;
import com.snapBuy.project.payload.CartDTO;
import com.snapBuy.project.payload.CartItemDTO;
import com.snapBuy.project.repositories.CartRepository;
import com.snapBuy.project.service.CartService;
import com.snapBuy.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller responsible for shopping cart management.
 * Provides APIs for creating carts, adding/removing products,
 * updating product quantities, and retrieving user cart information.
 */

@RestController
@RequestMapping("/api")
public class CartController {

    // Repository for cart data access

    @Autowired
    private CartRepository cartRepository;

    // Utility for retrieving logged-in user information

    @Autowired
    private AuthUtil authUtil;

    // Service layer handling cart business logic

    @Autowired
    private CartService cartService;


    /**
     * Create a new cart or update an existing cart with the provided cart items.
     */
    @PostMapping("/cart/create")
    public ResponseEntity<String> createOrUpdateCart(@RequestBody List<CartItemDTO> cartItems){
        String response = cartService.createOrUpdateCartWithItems(cartItems);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Add a product to the user's cart with a specified quantity.
     */
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieve all carts.
     */
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    /**
     * Retrieve the cart associated with the currently authenticated user.
     */
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    /**
     * Update product quantity in the user's cart.
     * Supports incrementing, decrementing, or removing products.
     */
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation) {

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    /**
     * Remove a specific product from a cart.
     */
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
