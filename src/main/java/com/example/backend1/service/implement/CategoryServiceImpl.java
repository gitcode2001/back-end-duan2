package com.example.backend1.service.implement;


import com.example.backend1.model.Category;
import com.example.backend1.repository.CategoryRepository;
import com.example.backend1.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        existingCategory.ifPresent(c -> {
            c.setName(category.getName());
            c.setDescription(category.getDescription());
            categoryRepository.save(c);
        });
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
