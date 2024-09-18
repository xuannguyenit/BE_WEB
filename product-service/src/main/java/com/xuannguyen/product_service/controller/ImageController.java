package com.xuannguyen.product_service.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.entity.Image;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ImageController {
    private static String UPLOAD_DIR  = System.getProperty("user.dir") + "/upload/photo/";
//    private static String UPLOAD_DIR  = System.getProperty("user.dir") + "/src/main/resources/static/photos/";

    @Autowired
    private ImageService imageService;


    @GetMapping("/")
    public ApiResponse getList(){
        List<Image> listImage = imageService.getAllImage();

        return  ApiResponse.builder()
                .result(listImage)
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse getImageById(@PathVariable("id") String id){
        return ApiResponse.builder()
                .result(imageService.getImageById(id))
                .build();
    }


    @PostMapping("/upload-file")
    @Operation(summary="Upload file lên database")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);;
        if (originalFilename != null && originalFilename.length() > 0) {
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("gif")
                    && !extension.equals("svg") && !extension.equals("jpeg")) {
                throw new AppException(ErrorCode.FILE_NOT_SUPPORT);
            }
            try {
                Image img = new Image();
                img.setName(file.getName());
                img.setSize(file.getSize());
                img.setType(extension);
                img.setData(file.getBytes());
                String uid = UUID.randomUUID().toString();
                String link = UPLOAD_DIR + uid + "." + extension;
                // Create file
                File serverFile = new File(link);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(file.getBytes());
                stream.close();

                imageService.save(img);
                return ResponseEntity.ok(img);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
            }
        }

        throw new AppException(ErrorCode.INVALID_FILE);

    }
}

