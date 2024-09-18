package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.entity.Image;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.repository.ImageRepository;
import com.xuannguyen.product_service.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {
    ImageRepository imageRepository;
    @Override
    public List<Image> getAllImage() {
        List<Image> images = imageRepository.findAll();
        return images;
    }

    @Override
    public Image getImageById(String id) {
        Image image = imageRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.IMAGE_NOT_EXITS));
        return image;
    }

    @Override
    public Image save(Image image) {

        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(String id) {
        if (!imageRepository.findById(id).isPresent()) {
            throw new AppException(ErrorCode.IMAGE_NOT_EXITS);
        }
        imageRepository.deleteById(id);
    }
}
