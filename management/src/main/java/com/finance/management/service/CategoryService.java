package com.finance.management.service;

import com.finance.management.model.Category;

import java.util.List;

public interface CategoryService {
    void insertCategory(List<Category> category);
    List<Category> getCategories(Category category);
}
