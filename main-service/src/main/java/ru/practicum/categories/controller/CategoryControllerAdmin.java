package ru.practicum.categories.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.CategoryServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryControllerAdmin {

    private final CategoryServiceAdmin categoryAdminService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody @NotNull @Valid NewCategoryDto categoryDto) {
        return new ResponseEntity<>(categoryAdminService.adminCreateCategory(categoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        categoryAdminService.adminDeleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{catId}")
    public ResponseEntity<Object> updateCategory(@RequestBody @NotNull @Valid NewCategoryDto categoryDto, @PathVariable Long catId) {
        return new ResponseEntity<>(categoryAdminService.adminUpdateCategory(categoryDto, catId), HttpStatus.OK);
    }
}
