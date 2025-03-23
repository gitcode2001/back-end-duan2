package com.example.backend1.service.implement;

import com.example.backend1.model.Image;
import com.example.backend1.repository.ImageRepository;
import com.example.backend1.service.IImageService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image updateImage(Long id, Image image) {
        Image existingImage = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
        existingImage.setImg(image.getImg());
        return imageRepository.save(existingImage);
    }

    @Override
    public void deleteImage(Long id) {
        Image existingImage = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
        imageRepository.delete(existingImage);
    }
}
