package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

public interface CategoryServiceAdmin {

    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId);
}
