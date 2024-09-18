package com.xuannguyen.oder.service.impl;


import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.ProductClientResponse;

import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.repository.CartItemRepository;
import com.xuannguyen.oder.repository.CartRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    ProductClient productClient;
    CartItemRepository cartItemRepository;

    // Lấy thông tin sản phẩm từ ProductClient
    public ProductClientResponse getProductClientResponse(String id) {
        ApiResponse<ProductClientResponse> apiResponseProductResponse = productClient.getProductById(id);
        if (apiResponseProductResponse == null || apiResponseProductResponse.getResult() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }

        return apiResponseProductResponse.getResult();
    }

    // Thêm sản phẩm vào giỏ hàng
    public Cart addItemToCart(String productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // lấy userId từ header

        // Tìm giỏ hàng với trạng thái ACTIVE cho người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setStatus("ACTIVE");  // Tạo giỏ hàng mới với trạng thái ACTIVE
                    return cartRepository.save(newCart);
                });

        // Lấy thông tin sản phẩm từ ProductClient
        ProductClientResponse productClientResponse = getProductClientResponse(productId);
        long productPrice = productClientResponse.getPrice();
        int quantity = 1;  // Số lượng mặc định là 1
        long totalPrice = productPrice * quantity;

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng hay chưa
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Nếu sản phẩm đã có, cập nhật số lượng và giá
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(productPrice * cartItem.getQuantity());  // Tính lại giá
        } else {
            // Nếu chưa có, thêm mới sản phẩm vào giỏ hàng
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(totalPrice);  // Thiết lập giá cho sản phẩm
            cart.getCartItems().add(cartItem);
        }

        // Cập nhật tổng giá của giỏ hàng
        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToLong(CartItem::getPrice)  // Tính tổng giá bằng cách cộng giá của từng sản phẩm
                .sum());

        // Lưu giỏ hàng sau khi cập nhật
        return cartRepository.save(cart);
    }

    // Cập nhật số lượng cho sản phẩm
    public Cart updateCartItemQuantity(String productId, int newQuantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();

            // Nếu số lượng mới là 0, xóa sản phẩm khỏi giỏ hàng
            if (newQuantity <= 0) {
                cart.getCartItems().remove(cartItem);
            } else {
                cartItem.setQuantity(newQuantity);
                ProductClientResponse productClientResponse = getProductClientResponse(productId);
                cartItem.setPrice(productClientResponse.getPrice() * newQuantity);  // Cập nhật giá với số lượng mới
            }
        } else {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }

        // Cập nhật tổng giá trị của giỏ hàng
        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToLong(CartItem::getPrice)
                .sum());

        return cartRepository.save(cart);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public Cart removeItemFromCart(String productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cart.getCartItems().remove(cartItem);
        } else {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }

        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToLong(CartItem::getPrice)
                .sum());

        return cartRepository.save(cart);
    }

    // Lấy tất cả giỏ hàng của người dùng (bao gồm cả các giỏ hàng đã checkout)
    public List<Cart> getAllCartsByUserId(String userId) {
        return cartRepository.findAllByUserId(userId);
    }

}

