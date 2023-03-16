package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.impl.CategoryServiceAdminImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryControllerAdmin {

    private final CategoryServiceAdminImpl categoryAdminService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody @NotNull @Valid NewCategoryDto categoryDto) {
        log.info("Запрос создания категории от администратора");
        return new ResponseEntity<>(categoryAdminService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("{catId}")
    public ResponseEntity<Object> updateCategory(@RequestBody @NotNull @Valid NewCategoryDto categoryDto, @PathVariable Long catId) {
        log.info("Запрос обновления категории от администратора - " + catId);
        return new ResponseEntity<>(categoryAdminService.updateCategory(categoryDto, catId), HttpStatus.OK);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Запрос удаления категории от администратора - " + catId);
        categoryAdminService.deleteCategory(catId);
    }
}
