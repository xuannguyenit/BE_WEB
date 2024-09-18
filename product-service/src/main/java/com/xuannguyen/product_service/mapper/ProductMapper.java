package com.xuannguyen.product_service.mapper;

import com.xuannguyen.product_service.dto.request.CreationProductRequest;
import com.xuannguyen.product_service.dto.response.ProductResponse;
import com.xuannguyen.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product post);


}
