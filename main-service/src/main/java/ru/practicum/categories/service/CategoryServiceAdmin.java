package ru.practicum.categories.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

public interface CategoryServiceAdmin {

    @Transactional
    CategoryDto createCategory(NewCategoryDto categoryDto);

    @Transactional
    void deleteCategory(Long categoryId);

    @Transactional
    CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId);
}
