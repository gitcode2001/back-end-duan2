package com.example.backend1.service.implement;


import com.example.backend1.model.Food;
import com.example.backend1.repository.FoodRepository;
import com.example.backend1.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService implements IFoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @Override
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

    @Override
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public Food updateFood(Long id, Food updatedFood) {
        return foodRepository.findById(id).map(food -> {
            food.setName(updatedFood.getName());
            food.setCategory(updatedFood.getCategory());
            food.setDescription(updatedFood.getDescription());
            food.setPrice(updatedFood.getPrice());
            food.setQuantity(updatedFood.getQuantity());
            food.setRating(updatedFood.getRating());
            food.setStatus(updatedFood.getStatus());
            food.setImageUrl(updatedFood.getImageUrl()); // Đảm bảo cập nhật ảnh món ăn
            return foodRepository.save(food);
        }).orElse(null);
    }

    @Override
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    @Override
    public List<Food> searchFoodByName(String name) {
        return foodRepository.findByNameContainingIgnoreCase(name);
    }
}
