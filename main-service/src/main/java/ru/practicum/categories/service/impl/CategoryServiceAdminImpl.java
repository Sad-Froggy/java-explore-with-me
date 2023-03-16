package ru.practicum.categories.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.categories.service.CategoryServiceAdmin;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.EwmObjectFinder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EwmObjectFinder finder;

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Optional<Category> categoryByName = categoryRepository.findByName(categoryDto.getName());
        if (categoryByName.isPresent()) {
            throw new DataConflictException("Категория с названием " + categoryDto.getName() + " уже существует");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        log.info("Создана категория " + category.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        List<Event> eventList = eventRepository.findAllByCategoryId(categoryId);
        if (!eventList.isEmpty()) {
            throw new DataConflictException("Невозможно удалить категорию если существуют события связянные с ней");
        }
        try {
            categoryRepository.deleteById(categoryId);
            log.info("Категория удалена");
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Категория с id " + categoryId + " не найдена");
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId) {
        finder.checkCategoryExistenceById(catId);
        categoryDto.setId(catId);
        try {
            Category updatedCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
            log.info("Обновлена Категория " + updatedCategory.getName());
            return CategoryMapper.toCategoryDto(updatedCategory);
        } catch (Exception e) {
            throw new DataConflictException(String.format("Невозможно изменить категорию id = %d! " +
                    "Ктегория c таким названием уже существует", catId));
        }
    }
}
