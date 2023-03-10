package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdmin {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CategoryDto adminCreateCategory(NewCategoryDto categoryDto) {
        Optional<Category> categoryByName = categoryRepository.findByName(categoryDto.getName());
        if (categoryByName.isPresent()) {
            throw new DataConflictException("Category with this name already exists");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void adminDeleteCategory(Long categoryId) {
        List<Event> eventList = eventRepository.findAllByCategoryId(categoryId);
        if (!eventList.isEmpty()) {
            throw new DataConflictException("There is event associated with this category");
        }
        try {
            categoryRepository.deleteById(categoryId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Category " + categoryId + " not found");
        }
    }

    @Transactional
    public CategoryDto adminUpdateCategory(NewCategoryDto categoryDto, Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category " + categoryDto + " not found"));
        Optional<Category> categoryByName = categoryRepository.findByName(category.getName());
        if (categoryByName.isPresent() && category.getId().equals(categoryByName.get().getId())) {
            throw new DataConflictException("Category with this name already exists");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}
