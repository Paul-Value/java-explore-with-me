package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto requestBody);

    void delete(long catId);

    CategoryDto update(CategoryDto requestBody);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(long catId);
}
