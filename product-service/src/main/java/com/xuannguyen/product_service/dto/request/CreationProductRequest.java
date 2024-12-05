package com.xuannguyen.product_service.dto.request;


import java.math.BigDecimal;
import java.util.Set;


import com.xuannguyen.product_service.entity.DiscountCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationProductRequest {


    @NotEmpty (message="Tên sản phẩm rỗng")

    @Size(min=1,max=50,message="Tên sản phẩm từ 1-50 ký tự")
    private String name;

    @NotNull(message = "Mô tả rỗng")
    @NotEmpty(message="Mô tả rỗng")

    @Size(min=5,max=1000,message="Mô tả sản phẩm từ 5-1000 ký tự")
    private String description;
    @NotNull(message = "Mô tả rỗng")
    @NotEmpty(message="Mô tả rỗng")

    @Size(min=5,max=400,message="Mô tả sản phẩm từ 5-1000 ký tự")
    private String shortDescription;

    @NotNull(message = "Giá tiền rỗng")
    @NotEmpty(message = "Giá tiền rỗng")

    @Size(min=0,message="Giá tiền sản phẩm lớn hơn 0")
    private long price;

    @NotNull(message = "Số lượng sản phẩm")
    @NotEmpty(message="Số lượng sản phẩm")

    @Size(min=0,message="Số lượng sản phẩm từ 0")
    private int quantity;

    @NotNull(message = "Danh mục rỗng")
    @NotEmpty(message = "Danh mục rỗng")

    private String categoryId;
    @NotNull(message = "thương hiệu rỗng")
    @NotEmpty(message = "thương hiệu rỗng")

    private String brandId;

    private String discountCodeId;

    @NotNull(message="Ảnh sản phẩm rỗng")
    @Schema(description="Mảng Id của hình ảnh",example="[1,2,3]")
    private Set<String> imageIds;

}

