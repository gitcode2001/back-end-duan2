package com.example.backend1.controller;

import com.example.backend1.model.Image;
import com.example.backend1.service.IImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/images")
public class ImageRestController {
    @Autowired
    private IImageService imageService;

    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody Image image) {
        Image createdImage = imageService.saveImage(image);
        return ResponseEntity.ok(createdImage);
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable Long id, @RequestBody Image image) {
        Image updatedImage = imageService.updateImage(id, image);
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
