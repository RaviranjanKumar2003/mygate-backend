package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.CategoryDto;
import com.example.demo.ECommerce.Eentities.Category;
import com.example.demo.ECommerce.ERepositories.CategoryRepository;
import com.example.demo.ECommerce.EServices.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryDto mapToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), category.getSocietyId());
    }

    private Category mapToEntity(CategoryDto dto) {
        return new Category(dto.getId(), dto.getName(), dto.getDescription(), dto.getSocietyId());
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category saved = categoryRepository.save(category);
        return mapToDto(saved);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category c : categories) {
            dtos.add(mapToDto(c));
        }
        return dtos;
    }

    @Override
    public List<CategoryDto> getCategoriesBySociety(Long societyId) {
        List<Category> categories = categoryRepository.findBySocietyId(societyId);
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category c : categories) {
            dtos.add(mapToDto(c));
        }
        return dtos;
    }
}