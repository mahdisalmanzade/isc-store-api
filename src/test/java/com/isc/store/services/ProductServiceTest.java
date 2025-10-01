package com.isc.store.services;

import com.isc.store.dtos.ProductDto;
import com.isc.store.entities.Category;
import com.isc.store.entities.Product;
import com.isc.store.mappers.ProductMapper;
import com.isc.store.repositories.CategoryRepository;
import com.isc.store.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId((byte) 1);
        category.setName("Electronics");

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming Laptop")
                .price(new BigDecimal("1500"))
                .category(category)
                .build();

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Laptop");
        productDto.setDescription("Gaming Laptop");
        productDto.setPrice(new BigDecimal("1500"));
        productDto.setCategoryId((byte) 1);
    }

    @Test
    void testGetAllProductsWithoutCategory() {
        when(productRepository.findAllWithCategory()).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getAllProducts(null);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAllWithCategory();
        verify(productMapper, times(1)).mapToProductDto(product);
    }

    @Test
    void testGetAllProductsWithCategory() {
        when(productRepository.findByCategoryId((byte) 1)).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getAllProducts((byte) 1);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategoryId((byte) 1);
        verify(productMapper, times(1)).mapToProductDto(product);
    }

    @Test
    void testGetProductFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productService.getProduct(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getName());
    }

    @Test
    void testGetProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProductDto> response = productService.getProduct(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateProductSuccess() {
        when(categoryRepository.findById((byte) 1)).thenReturn(Optional.of(category));
        when(productMapper.mapToProductEntity(productDto)).thenReturn(product);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<ProductDto> response = productService.createProduct(productDto, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        verify(productRepository).save(product);
    }

    @Test
    void testCreateProductCategoryNotFound() {
        when(categoryRepository.findById((byte) 1)).thenReturn(Optional.empty());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<ProductDto> response = productService.createProduct(productDto, uriBuilder);

        assertEquals(400, response.getStatusCodeValue());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateProductSuccess() {
        when(categoryRepository.findById((byte) 1)).thenReturn(Optional.of(category));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<ProductDto> response = productService.updateProduct(1L, productDto);

        assertEquals(200, response.getStatusCodeValue());
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProductNotFound() {
        when(categoryRepository.findById((byte) 1)).thenReturn(Optional.of(category));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProductDto> response = productService.updateProduct(1L, productDto);

        assertEquals(404, response.getStatusCodeValue());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateProductCategoryNotFound() {
        when(categoryRepository.findById((byte) 1)).thenReturn(Optional.empty());

        ResponseEntity<ProductDto> response = productService.updateProduct(1L, productDto);

        assertEquals(400, response.getStatusCodeValue());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testDeleteProductSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Void> response = productService.deleteProduct(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = productService.deleteProduct(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(productRepository, never()).delete(any());
    }
}
