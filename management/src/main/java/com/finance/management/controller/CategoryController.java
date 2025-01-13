package com.finance.management.controller;

import com.finance.management.model.Category;
import com.finance.management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public ResponseEntity<String> insertCategory(@RequestBody List<Category> category) {
        categoryService.insertCategory(category);
        return ResponseEntity.ok("Category added successfully");
    }

    @GetMapping("/categories")
    public List<Category> getCategory(Category category) {
        return categoryService.getCategories(category);
    }
}
