package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.dtos.*;
import com.cwz.electronic.store.services.FileService;
import com.cwz.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto dto = productService.create(productDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto dto = productService.update(productDto, productId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Successfully deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get single
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        ProductDto dto = productService.getSingle(productId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //get all
    @GetMapping("/all")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(productService.getAll(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get all when live
    @GetMapping("/all/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductWhileLive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(productService.getAllLive(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    //get all by title
    @GetMapping("/all/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductContainingThis(@PathVariable String subTitle, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(productService.getAllByTitle(subTitle,pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    //upload image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/product/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getSingle(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder()
                .imageName(updatedProduct.getProductImageName())
                .success(true).status(HttpStatus.CREATED)
                .message("successfully uploaded").build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //serve image
    @GetMapping(value = "/image/product/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getSingle(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
