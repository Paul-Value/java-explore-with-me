package ru.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.DeletingCategoryWithLinkedEventsException;
import ru.practicum.service.category.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid
                              @RequestBody NewCategoryDto requestBody) {
        log.info("==> Create new category: {}", requestBody);
        return service.create(requestBody);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive
                       @PathVariable long catId) {
        log.info("==> Delete category: {}", catId);
        try {
            service.delete(catId);
        } catch (DataIntegrityViolationException e) {
            throw new DeletingCategoryWithLinkedEventsException("Deleting a category with linked events is not possible", e);
        }
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@Positive
                              @PathVariable long catId,
                              @RequestBody CategoryDto requestBody) {
        requestBody.setId(catId);
        log.info("==> Update category: {}", requestBody);
        return service.update(requestBody);
    }
}
