package com.cwz.electronic.store.services.Impl;

import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.ProductDto;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.Category;
import com.cwz.electronic.store.entities.Product;
import com.cwz.electronic.store.entities.User;
import com.cwz.electronic.store.exception.ResourceNotFoundException;
import com.cwz.electronic.store.helper.HelperForPR;
import com.cwz.electronic.store.repositories.CategoryRepository;
import com.cwz.electronic.store.repositories.ProductRepository;
import com.cwz.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagePath;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        Product savedProduct = productRepository.save(product);
        ProductDto dto = mapper.map(savedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found!!"));
//        product.setProductId(productDto.getProductId());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setProductImageName(productDto.getProductImageName());
        Product updatedProduct = productRepository.save(product);
        ProductDto dto = mapper.map(updatedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found!!"));
        String fullPath = imagePath + product.getProductImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found!!"));
        ProductDto dto = mapper.map(product, ProductDto.class);
        return dto;
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);

        PageableResponse<ProductDto> response = HelperForPR.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        PageableResponse<ProductDto> response = HelperForPR.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        PageableResponse<ProductDto> response = HelperForPR.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        Product product = mapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductDto dto = mapper.map(savedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public ProductDto updateCategoryOfProduct(String categoryId, String productId) {
        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found."));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("This category does not exist."));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        ProductDto dto = mapper.map(updatedProduct, ProductDto.class);
        return dto;
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("This category does not exist."));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        PageableResponse<ProductDto> response = HelperForPR.getPageableResponse(productPage, ProductDto.class);
        return response;
    }
}
