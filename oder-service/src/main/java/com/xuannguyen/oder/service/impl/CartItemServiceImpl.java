package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.CartItemResponse;
import com.xuannguyen.oder.dto.respone.ProductClientResponse;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.repository.CartItemRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.CartItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {
    CartItemRepository cartItemRepository;
    ProductClient productClient;


}
