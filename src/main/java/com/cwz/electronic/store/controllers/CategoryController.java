package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.dtos.ApiResponseMessage;
import com.cwz.electronic.store.dtos.CategoryDto;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.ProductDto;
import com.cwz.electronic.store.services.CategoryService;
import com.cwz.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        CategoryDto createdCategoryDto = categoryService.create(categoryDto);
        return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
    }
    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
        CategoryDto updatedCategoryDto = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategoryDto,HttpStatus.OK);
    }
    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Successfully deleted!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    //get all
    @GetMapping("/all")
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<CategoryDto> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
    //get single
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    //create product with category
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    ){
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }
    //update category of product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDto> handleUpdateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
        ProductDto updatedProductDto = productService.updateCategoryOfProduct(categoryId, productId);
        return new ResponseEntity<>(updatedProductDto,HttpStatus.OK);
    }
    //get all products of a category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> handleGetAllProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProductsByCategory = productService.getAllProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allProductsByCategory,HttpStatus.OK);
    }

}
