package com.finance.management.service.impl;

import com.finance.management.mapper.CategoryMapper;
import com.finance.management.model.Category;
import com.finance.management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper){
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
    }

    @Override
    public List<Category> getCategories(Category category) {
        return categoryMapper.getCategories(category);
    }
}
