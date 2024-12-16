package com.xuannguyen.oder.dto.respone;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenueResponse {
    private int day; // Ngày trong tháng
    private long totalRevenue; // Tổng doanh thu trong ngày
}
