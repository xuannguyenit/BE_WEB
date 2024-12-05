package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.request.OrderDetailRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.entity.OrderDetail;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderdetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/create")
    public ApiResponse<OrderDetail> createOderDetail(@RequestBody OrderDetailRequest orderDetail) {
        return ApiResponse.<OrderDetail>builder()
                .message("create order detail success")
                .result(orderDetailService.createOrderDetail(orderDetail))
                .build();
    }
    @PutMapping ("/update/{id}")
    public ApiResponse<OrderDetail> updateOderDetail(@PathVariable Integer id, @RequestBody OrderDetailRequest orderDetail) {
        return ApiResponse.<OrderDetail>builder()
                .message("update order detail success")
                .result(null)
                .build();
    }
    // lấy ra danh sách orderdetail của 1 order
    @GetMapping ("/getbyorderid/{id}")
    public ApiResponse<List<OrderDetail>> getOderDetailByOderId(@PathVariable Integer id) {
        return ApiResponse.<List<OrderDetail>>builder()
                .message("success")
                .result(null)
                .build();
    }
    // lấy ra danh sách orderdetail
    @GetMapping("/get")
    public ApiResponse<List<OrderDetail>> getAllOderDetail() {
        return ApiResponse.<List<OrderDetail>>builder()
                .message("success")
                .result(null)
                .build();
    }
    // lấy ra oderdetail theo id
    @GetMapping("/get/{id}")
    public ApiResponse<OrderDetail> getOderDetailById(@PathVariable Integer id) {
        return ApiResponse.<OrderDetail>builder()
                .message("success")
                .result(null)
                .build();
    }



}
