package com.cwz.electronic.store.services.Impl;

import com.cwz.electronic.store.dtos.CategoryDto;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.entities.Category;
import com.cwz.electronic.store.exception.ResourceNotFoundException;
import com.cwz.electronic.store.helper.HelperForPR;
import com.cwz.electronic.store.repositories.CategoryRepository;
import com.cwz.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto newCategoryDto = mapper.map(savedCategory, CategoryDto.class);
        return newCategoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given ID!!!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        CategoryDto updatedDto = mapper.map(updatedCategory, CategoryDto.class);
        return updatedDto;
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given ID!!!"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response = HelperForPR.getPageableResponse(page, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given ID!!!"));
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        return categoryDto;
    }
}
