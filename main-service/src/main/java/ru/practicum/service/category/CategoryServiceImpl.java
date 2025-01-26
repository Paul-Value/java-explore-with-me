package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UniqueNameCategoriesException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    //    Admin
    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto requestBody) {
        log.debug("==> Create new category: {}", requestBody);
        existCategoryWithName(requestBody.getName());
        Category model = repository.save(CategoryMapper.dtoToModel(requestBody));
        log.debug("<== New category created : {}", model);
        return CategoryMapper.modelToDto(model);
    }

    private void existCategoryWithName(String name) {
        if (repository.existsByName(name)) {
            throw new UniqueNameCategoriesException("Category with name " + name + " already exists");
        }
    }

    @Transactional
    @Override
    public void delete(long catId) {
        log.debug("==> Delete category: {}", catId);
        repository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto update(CategoryDto requestBody) {
        log.debug("==> Update category: {}", requestBody);
        Category category = repository.findByName(requestBody.getName());
        if (category != null) {
            if (!category.getId().equals(requestBody.getId())) {
                throw new UniqueNameCategoriesException("Category with name " + requestBody.getName() + " already exists");
            }
        }
        Category model = repository.save(CategoryMapper.dtoToModel(requestBody));
        log.debug("<== Category updated : {}", model);
        return CategoryMapper.modelToDto(model);
    }

    //    Public
    @Override
    public List<CategoryDto> findAll(int from, int size) {
        log.debug("==> Find all Categories from {}, size {}", from, size);
        List<Category> result = repository.findAllLimit(from, size);
        log.debug("<== Found all Categories from {}, size {}", from, size);
        return result.stream()
                .map(CategoryMapper::modelToDto)
                .toList();
    }

    @Override
    public CategoryDto findById(long catId) {
        log.debug("==> Find Category with id {}", catId);
        Category result = repository.findById(catId).orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        log.debug("<== Found Category with id {}", catId);
        return CategoryMapper.modelToDto(result);
    }
}
