package com.finance.management.mapper;

import com.finance.management.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    void insertCategory(List<Category> category);
    List<Category> getCategories(Category category);
}
