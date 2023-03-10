package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

import java.util.Collection;


public interface CategoryService {
    Collection<CategoryDto> getCategories(Integer from, Integer size);

    Category getCategoryById(Long catId);

    Category postCategory(NewCategoryDto newCategory);

    Category patchCategory(NewCategoryDto newCategory, Long catId);

    void deleteCategory(Long catId);
}
