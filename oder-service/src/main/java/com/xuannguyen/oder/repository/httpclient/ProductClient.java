package com.xuannguyen.oder.repository.httpclient;

import com.xuannguyen.oder.configuration.AuthenticationRequestInterceptor;
import com.xuannguyen.oder.dto.request.ProductUpdateQuantityRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.DiscountCode;
import com.xuannguyen.oder.dto.respone.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient( name = "product-service",
        url = "${app.services.product-service}",
        contextId = "productServiceClient",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProductClient {
    @GetMapping(value = "/prod/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Product> getProduct(@PathVariable String id);
    // API lấy thông tin discount code
    @GetMapping(value = "/discount/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DiscountCode> getDiscountCode (@PathVariable("id") String id);
    // gọi phương thức update lại số lượng sản phẩm sau khi bán
    @PutMapping (value = "/prod/update/quantity/afterorder" , produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Product> updateProductQuantityAfterOrder (@RequestBody ProductUpdateQuantityRequest request);

}
