package com.example.backend1.controller;

import com.example.backend1.model.Category;
import com.example.backend1.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    // ✅ Luôn trả về danh sách, kể cả rỗng, tránh lỗi frontend .map()
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories); // KHÔNG dùng .noContent()
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.ok("✅ Danh mục đã được tạo thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Optional<Category> existingCategory = categoryService.findById(id);
        if (existingCategory.isPresent()) {
            categoryService.updateCategory(id, category);
            return ResponseEntity.ok("✅ Danh mục đã được cập nhật!");
        }
        return ResponseEntity.status(404).body("❌ Không tìm thấy danh mục để cập nhật!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("🗑️ Xoá danh mục thành công!");
        }
        return ResponseEntity.status(404).body("❌ Không tìm thấy danh mục để xoá!");
    }
}
