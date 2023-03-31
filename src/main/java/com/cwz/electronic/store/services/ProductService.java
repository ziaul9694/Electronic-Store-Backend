package com.cwz.electronic.store.services;

import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);
    //update
    ProductDto update(ProductDto productDto, String productId);
    //delete
    void delete(String productId);
    //get single
    ProductDto getSingle(String productId);
    //get all
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get all when live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search product
    PageableResponse<ProductDto> getAllByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
    //create category into product
    ProductDto createWithCategory(ProductDto productDto, String categoryId);
    //update category of product
    ProductDto updateCategoryOfProduct(String categoryId,String productId);
    //get all products from a category
    PageableResponse<ProductDto> getAllProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
