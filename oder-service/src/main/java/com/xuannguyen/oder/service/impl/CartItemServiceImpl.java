package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.dto.request.UpdateCartItemRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.CartItemResponse;
import com.xuannguyen.oder.dto.respone.DiscountCode;
import com.xuannguyen.oder.dto.respone.Product;
import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.repository.CartItemRepository;
import com.xuannguyen.oder.repository.CartRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    ProductClient productClient;
    // get sản phẩm theo id
    private Product getProductClientResponse(String id) {
        ApiResponse<Product> productResponse = productClient.getProduct(id);
        if (productResponse == null || productResponse.getResult() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        return productResponse.getResult();
    }
    private DiscountCode getDiscountCodeResponse(String id) {
        ApiResponse<DiscountCode> apiResponseDiscountCodeResponse = productClient.getDiscountCode(id);
        if (apiResponseDiscountCodeResponse == null || apiResponseDiscountCodeResponse.getResult() == null) {
            return null;
        }
        return apiResponseDiscountCodeResponse.getResult();
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @Override
    public CartItemResponse addToCart(String productId) {
        // Lấy userId từ SecurityContext
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED); // Ném ngoại lệ nếu chưa đăng nhập
        }

        // Lấy giỏ hàng của user hoặc tạo mới nếu chưa có
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();

                    newCart.setTotalPrice(0L);
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        Product product = getProductClientResponse(productId);
        if (product == null || product.getQuantity()<1) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        System.out.println(product);
        // Kiểm tra giảm giá của sản phẩm
        long discountPercentage = 0; // Mặc định không giảm giá
        if (product.getDiscountCode() != null) {
            DiscountCode discountCode = getDiscountCodeResponse(product.getDiscountCode().getId());
            if (discountCode != null) {
                discountPercentage = discountCode.getDiscountPercentage();
            }
        }

        // Tính giá sau khi áp dụng giảm giá
        long productPriceAfterDiscount = product.getPrice() - (product.getPrice() * discountPercentage / 100);

        // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

//        if (cartItem != null) {
//            // Nếu sản phẩm đã có trong giỏ, cập nhật số lượng và tổng giá
//            int updatedQuantity = cartItem.getProductQuantity() + 1; // Tăng 1 đơn vị
//
//            cartItem.setProductQuantity(updatedQuantity);
//            cartItem.setTotalPrice(productPriceAfterDiscount * updatedQuantity);
//
//            cartItemRepository.save(cartItem); // lưu thông tin vào giỏ hàng
//            List<CartItem> list = cart.getCartItems();
//            long totalPrice = 0;
//            for (CartItem cartItem1 : list) {
//                totalPrice += cartItem1.getTotalPrice();
//            }
//
////            cart.setTotalPrice(cartItem.getTotalPrice());
//            cartItem.setTotalPrice(totalPrice);
//            cartRepository.save(cart);
//            return CartItemResponse.builder()
//                    .id(cartItem.getId())
//                    .cartId(cartItem.getCart().getId())
//                    .productId(cartItem.getProductId())
//                    .productName(cartItem.getProductName())
//                    .productPrice(cartItem.getProductPrice())
//                    .discountPercentage(cartItem.getDiscountPercentage())
//                    .productQuantity(cartItem.getProductQuantity())
//                    .totalPrice(cartItem.getTotalPrice())
//                    .userId(cartItem.getUserId())
//                    .build();
//        }
//
//        // Nếu sản phẩm chưa có trong giỏ, thêm mới
//        cartItem = new CartItem();
//        cartItem.setProductId(productId);
//        cartItem.setProductName(product.getName());
//        cartItem.setCart(cart);
//        cartItem.setProductPrice(product.getPrice());
//        cartItem.setProductQuantity(1);
//        cartItem.setDiscountPercentage(discountPercentage);
//        cartItem.setTotalPrice(productPriceAfterDiscount * 1);
//        cartItem.setUserId(userId);
//        cartItemRepository.save(cartItem); // Lưu lại
//
//        long sumTotalPrice = 0;
//        List<CartItem> list = cart.getCartItems();
//        for (CartItem cartItem1 : list) {
//            sumTotalPrice += cartItem1.getTotalPrice();
//        }
////        cart.setTotalPrice(cartItem.getTotalPrice());
//        cart.setTotalPrice(sumTotalPrice);
//        cartRepository.save(cart);
//
//        return CartItemResponse.builder()
//                .id(cartItem.getId())
//                .cartId(cartItem.getCart().getId())
//                .productId(cartItem.getProductId())
//                .productName(cartItem.getProductName())
//                .productPrice(cartItem.getProductPrice())
//                .discountPercentage(cartItem.getDiscountPercentage())
//                .productQuantity(cartItem.getProductQuantity())
//                .totalPrice(cartItem.getTotalPrice())
//                .userId(cartItem.getUserId())
//                .build();
        if (cartItem != null) {
            int updatedQuantity = cartItem.getProductQuantity() + 1;
            cartItem.setProductQuantity(updatedQuantity);
            cartItem.setTotalPrice(productPriceAfterDiscount * updatedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProductId(productId);
            cartItem.setProductName(product.getName());
            cartItem.setCart(cart);
            cartItem.setProductPrice(product.getPrice());
            cartItem.setProductQuantity(1);
            cartItem.setDiscountPercentage(discountPercentage);
            cartItem.setTotalPrice(productPriceAfterDiscount);
        }
        cartItem.setUserId(userId);

        cartItemRepository.save(cartItem);

        // Cập nhật tổng giá của giỏ hàng
        long totalPrice = cartItemRepository.findAllByCartId(cart.getId())
                .stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .productPrice(cartItem.getProductPrice())
                .discountPercentage(cartItem.getDiscountPercentage())
                .productQuantity(cartItem.getProductQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .userId(cartItem.getUserId())
                .build();
    }


    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void removeFromCart(String cartId, String productId) {
        CartItem item = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItemRepository.delete(item);
    }
    // update lại cartItem theo id và số lượng
    @Override
    public CartItemResponse updateCart(UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(request.getCartId()).orElseThrow(()->new AppException(ErrorCode.CART_NOT_EXITS));
        cartItem.setProductQuantity(request.getQuantity());
        //tính lại tổng giá cho sản phẩm
        long discountPercentage = 0; // Mặc định không giảm giá
        if (cartItem.getDiscountPercentage() > 0) {
            discountPercentage = cartItem.getDiscountPercentage();
        }
        // Tính giá sau khi áp dụng giảm giá
        long productPriceAfterDiscount = cartItem.getProductPrice() - (cartItem.getProductPrice() * discountPercentage / 100);

        cartItem.setTotalPrice(productPriceAfterDiscount * request.getQuantity());
        cartItemRepository.save(cartItem);
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .productPrice(cartItem.getProductPrice())
                .discountPercentage(cartItem.getDiscountPercentage())
                .productQuantity(cartItem.getProductQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .userId(cartItem.getUserId())
                .build();
    }

    @Override
    public void deleteCart(String cartId) {
        cartItemRepository.deleteById(cartId);
    }
}
