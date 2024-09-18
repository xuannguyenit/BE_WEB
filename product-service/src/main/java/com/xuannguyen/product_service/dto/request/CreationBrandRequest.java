package com.xuannguyen.product_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreationBrandRequest {
    @NotNull(message = "Tên Thương Hiệu rỗng")
    @NotEmpty(message = "Tên Thương Hiệu rỗng")
    @Size(min=1,max=50,message="Độ dài tên thương hiệu từ 5-50 ký tự")
    private String name;
}
