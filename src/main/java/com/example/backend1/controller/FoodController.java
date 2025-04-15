package com.example.backend1.controller;

import com.example.backend1.model.Food;
import com.example.backend1.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin("*")
public class FoodController {
    @Autowired
    private IFoodService foodService;

    // Lấy danh sách tất cả món ăn
    @GetMapping
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Optional<Food> food = foodService.getFoodById(id);
        return food.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm món ăn mới
    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        return ResponseEntity.ok(foodService.saveFood(food));
    }

    // Cập nhật thông tin món ăn
    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable Long id, @RequestBody Food food) {
        Food updatedFood = foodService.updateFood(id, food);
        if (updatedFood != null) {
            return ResponseEntity.ok(updatedFood);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa món ăn theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        if (!foodService.getFoodById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    // Tìm kiếm món ăn theo tên
    @GetMapping("/search")
    public List<Food> searchFoodByName(@RequestParam String name) {
        return foodService.searchFoodByName(name);
    }
}

