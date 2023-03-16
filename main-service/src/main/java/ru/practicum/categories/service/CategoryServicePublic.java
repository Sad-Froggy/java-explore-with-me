package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {

    List<CategoryDto> getCategories(Long from, Integer size);

    CategoryDto getCategoryById(Long catId);

}
