package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.service.CategoryService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerPub {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> get(@RequestParam (defaultValue = "0") int from,
                                       @RequestParam (defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }
}
