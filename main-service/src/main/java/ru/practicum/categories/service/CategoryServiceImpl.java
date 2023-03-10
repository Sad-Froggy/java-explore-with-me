package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Collection<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "This category not found")
        );
    }

    public Category postCategory(NewCategoryDto newCategory) {
        try {
            return categoryRepository.save(CategoryMapper.toCategory(newCategory));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This category name is duplicated", exception);
        }
    }

    public Category patchCategory(NewCategoryDto categoryDto, Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("придумаю позже"));
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long catId) {
        try {
            categoryRepository.deleteById(catId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This category isn't empty", exception);
        }
    }
}
