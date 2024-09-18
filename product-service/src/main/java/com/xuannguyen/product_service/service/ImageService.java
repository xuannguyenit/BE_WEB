package com.xuannguyen.product_service.service;

import com.xuannguyen.product_service.entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> getAllImage();

    Image getImageById(String id);

    Image save(Image image);

    void deleteImage(String id);
}
