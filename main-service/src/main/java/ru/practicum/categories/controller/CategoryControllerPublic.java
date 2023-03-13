package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.service.impl.CategoryServicePublicImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerPublic {

    private final CategoryServicePublicImpl categoryService;

    @GetMapping
    public ResponseEntity<Object> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Long from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос списка категорий от пользователя");
        return new ResponseEntity<>(categoryService.getCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable Long catId) {
        log.info("Запрос категории по id от пользователя - " + catId);
        return new ResponseEntity<>(categoryService.getCategory(catId), HttpStatus.OK);
    }
}
