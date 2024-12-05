package com.xuannguyen.oder.dto.respone;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class DiscountCode {
    private String id;
    private String code;
    private long discountPercentage;

}
