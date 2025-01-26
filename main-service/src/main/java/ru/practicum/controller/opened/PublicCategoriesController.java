package ru.practicum.controller.opened;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Slf4j
public class PublicCategoriesController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> findAll(@PositiveOrZero
                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @PositiveOrZero
                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("==> Find all categories from={}, size={}", from, size);
        return service.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto find(@Positive
                            @PathVariable long catId) {
        log.info("==> Find category by categoryId={}", catId);
        return service.findById(catId);
    }
}
