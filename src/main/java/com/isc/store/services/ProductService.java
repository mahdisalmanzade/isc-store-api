package com.isc.store.services;

import com.isc.store.dtos.ProductDto;
import com.isc.store.entities.Product;
import com.isc.store.exceptions.CategoryNotFoundException;
import com.isc.store.exceptions.ProductNotFoundException;
import com.isc.store.mappers.ProductMapper;
import com.isc.store.repositories.CategoryRepository;
import com.isc.store.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDto> getAllProducts (Byte categoryId) {

        List<Product> products;
        if(categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            // custom query to stop Hibernate default behavior
            products = productRepository.findAllWithCategory();
//            products = productRepository.findAll();
        }
        return products.stream().map(product -> productMapper.mapToProductDto(product)).toList();


    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return productMapper.mapToProductDto(product);
    }


    @PostMapping
    public ProductDto createProduct(ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategoryId()));

        Product product = productMapper.mapToProductEntity(productDto);
        product.setCategory(category);
        Product saved = productRepository.save(product);

        return productMapper.mapToProductDto(saved);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategoryId()));

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.update(productDto, existingProduct);
        existingProduct.setCategory(category);

        Product saved = productRepository.save(existingProduct);
        return productMapper.mapToProductDto(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }
}
