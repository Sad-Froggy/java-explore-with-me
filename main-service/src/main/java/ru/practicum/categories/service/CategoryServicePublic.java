package ru.practicum.categories.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {

    @Transactional
    List<CategoryDto> getCategories(Long from, Integer size);

    @Transactional
    CategoryDto getCategory(Long catId);

}
