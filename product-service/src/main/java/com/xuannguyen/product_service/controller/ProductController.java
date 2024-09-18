package com.xuannguyen.product_service.controller;
import com.xuannguyen.product_service.dto.request.CreationProductRequest;
import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.dto.response.ProductResponse;
import com.xuannguyen.product_service.entity.Product;
import com.xuannguyen.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/prod")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/")
    @Operation(summary="Lấy ra danh sách sản phẩm")
    public ApiResponse<List<Product>> getList(){
        List<Product> list = productService.getList();
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);

        return apiResponse;
    }


    @GetMapping("/newest/{number}")
    @Operation(summary="Lấy ra danh sách sản phẩm mới nhất giới hạn số lượng = number")
    public ApiResponse<List<Product>> getListNewst(@PathVariable int number){
        List<Product> list =productService.getListNewst(number);
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);
        return apiResponse;
    }

    @GetMapping("/price")
    @Operation(summary="Lấy ra danh sách 8 sản phẩm có giá từ thấp nhất đến cao")
    public ApiResponse<List<Product>> getListByPrice(){
        List<Product> list = productService.getListByPrice();
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);
        return apiResponse;
    }

    @GetMapping("/related/{id}")
    @Operation(summary="Lấy ra ngẫu nhiên 4 sản phẩm bằng category_id")
    public ApiResponse<List<Product>> getListRelatedProduct(@PathVariable String id){
        List<Product> list = productService.findRelatedProduct(id);
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);
        return apiResponse;
    }

    @GetMapping("/category/{id}")
    @Operation(summary="Lấy ra danh sách sản phẩm bằng id của danh mục")
    public ApiResponse<List<Product>> getListProductByCategory(@PathVariable String id){
        List<Product> list =  productService.getListProductByCategory(id);
        ApiResponse <List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);
        return apiResponse;
    }

    @GetMapping("/range")
    @Operation(summary="Lấy ra danh sách sản phẩm ở các mức giá từ min đến max")
    public ApiResponse<List<Product>> getListProductByPriceRange(@RequestParam("id") String id,@RequestParam("min") int min, @RequestParam("max") int max){
        List<Product> list = productService.getListByPriceRange(id, min, max);
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(list);

        return apiResponse;
    }

    @GetMapping("/{id}")
    @Operation(summary="Lấy sản phẩm bằng id")
    public ApiResponse<Product> getProduct(@PathVariable String id){
        Product product = productService.getProductById(id);
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setResult(product);
        return apiResponse;
    }

    @GetMapping("/search")
    @Operation(summary="Tìm kiếm sản phẩm bằng keyword")
    public ApiResponse<List<Product>> searchProduct(@RequestParam("keyword") String keyword){
        List<Product> list = productService.searchProduct(keyword);
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        return apiResponse;
    }

    @PostMapping("/create")
    @Operation(summary="Tạo mới sản phẩm")
    public ApiResponse<Product> createProduct(@RequestBody CreationProductRequest request){
        Product product = productService.createProduct(request);
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setResult(product);
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    @Operation(summary="Tìm sản phẩm bằng id và cập nhật sản phẩm đó")
    public ApiResponse<Product> updateProduct(@PathVariable String id,@RequestBody CreationProductRequest request){
        Product product = productService.updateProduct(id, request);
        ApiResponse apiResponse = new ApiResponse<Product>();
        apiResponse.setResult(product);
        apiResponse.setMessage("Update successful");

        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Xóa sản phẩm bằng id")
    public ApiResponse<Void> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        ApiResponse <Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("xóa thành công sản phẩm");
        return apiResponse;
    }
    // phân trang
    @GetMapping("/getallproduct")
    @Operation(summary = "Lấy tất cả sản phẩm với phân trang")
    public ApiResponse<PageResponse<Product>> getAllProduct(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        // Spring phân trang bắt đầu từ 0, vì vậy bạn cần giảm giá trị của page đi 1
        PageResponse<Product> pageResponse = productService.getAllProductPagination(page - 1, size);

        return ApiResponse.<PageResponse<Product>>builder()
                .result(pageResponse)
                .build();
    }
    // update lại số lượng của sản phẩm khi mua hàng
    // Cập nhật số lượng sản phẩm sau khi đơn hàng thành công
    @PutMapping("/update-quantity")
    public ApiResponse<Product> updateProductAfterOrderSuccess(@RequestParam String productId, @RequestParam int newQuantity) {
        // Gọi service để cập nhật số lượng sản phẩm
        Product updatedProduct = productService.updateProductQuantity(productId, newQuantity);

        // Trả về phản hồi API
        return ApiResponse.<Product>builder()
                .result(updatedProduct)
                .build();
    }
}

