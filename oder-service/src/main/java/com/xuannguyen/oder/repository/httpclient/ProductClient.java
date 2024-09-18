package com.xuannguyen.oder.repository.httpclient;

import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( name = "product-service",
        url = "${app.services.product-service}")
public interface ProductClient {
    @GetMapping(value = "/prod/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProductClientResponse> getProductById(@PathVariable("id") String id);

    @PutMapping (value = "/prod/update-quantity", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProductClientResponse> updateProductAfterOrderSuccess (@RequestParam String productId, @RequestParam int newQuantity);

}
