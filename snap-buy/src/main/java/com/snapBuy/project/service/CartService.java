package com.snapBuy.project.service;

import com.snapBuy.project.payload.CartDTO;
import com.snapBuy.project.payload.CartItemDTO;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service interface for shopping cart management.
 * Provides operations for:
 * - Adding products to cart
 * - Retrieving cart information
 * - Updating cart item quantities
 * - Removing products from cart
 */

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    String createOrUpdateCartWithItems(List<CartItemDTO> cartItems);
}