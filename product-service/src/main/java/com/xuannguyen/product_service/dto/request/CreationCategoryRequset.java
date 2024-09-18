package com.xuannguyen.product_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationCategoryRequset {
    @NotNull(message = "Tên danh mục rỗng")
    @NotEmpty(message = "Tên danh mục rỗng")
    @Size(min=5,max=50,message="Độ dài danh mục từ 5-50 ký tự")
    private String name;
}

