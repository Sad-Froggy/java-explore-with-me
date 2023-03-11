package ru.practicum.categories.mapper;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto source) {
        return Category.builder()
                .name(source.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category source) {
        return CategoryDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

}
