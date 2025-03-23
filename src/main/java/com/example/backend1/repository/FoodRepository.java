package com.example.backend1.repository;


import com.example.backend1.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByNameContainingIgnoreCase(String name);
}
