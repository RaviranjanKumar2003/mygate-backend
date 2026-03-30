package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategories();

    List<CategoryDto> getCategoriesBySociety(Long societyId);
}