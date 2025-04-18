package com.example.backend1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopFoodDTO {
    private Long id;
    private String name;
    private Long sold;
}
