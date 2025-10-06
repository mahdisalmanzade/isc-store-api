package com.isc.store.services;

import com.isc.store.dtos.ProductDto;
import com.isc.store.entities.Category;
import com.isc.store.entities.Product;
import com.isc.store.exceptions.CategoryNotFoundException;
import com.isc.store.exceptions.ProductNotFoundException;
import com.isc.store.mappers.ProductMapper;
import com.isc.store.repositories.CategoryRepository;
import com.isc.store.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------- getAllProducts --------------------
    @Test
    void testGetAllProducts_WithCategory() {
        Byte categoryId = 1;
        Product product = new Product();
        ProductDto dto = new ProductDto();

        when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(dto);

        List<ProductDto> result = productService.getAllProducts(categoryId);

        assertEquals(1, result.size());
        verify(productRepository).findByCategoryId(categoryId);
        verify(productMapper).mapToProductDto(product);
    }

    @Test
    void testGetAllProducts_WithoutCategory() {
        Product product = new Product();
        ProductDto dto = new ProductDto();

        when(productRepository.findAllWithCategory()).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(dto);

        List<ProductDto> result = productService.getAllProducts(null);

        assertEquals(1, result.size());
        verify(productRepository).findAllWithCategory();
        verify(productMapper).mapToProductDto(product);
    }

    // -------------------- getProduct --------------------
    @Test
    void testGetProduct_Success() {
        Long productId = 1L;
        Product product = new Product();
        ProductDto dto = new ProductDto();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(dto);

        ProductDto result = productService.getProduct(productId);

        assertNotNull(result);
        verify(productRepository).findById(productId);
        verify(productMapper).mapToProductDto(product);
    }

    @Test
    void testGetProduct_NotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(productId));
    }

    // -------------------- createProduct --------------------
    @Test
    void testCreateProduct_Success() {
        ProductDto dto = new ProductDto();
        dto.setCategoryId((byte) 1);

        Product product = new Product();
        Product savedProduct = new Product();
        Category category = new Category();
        ProductDto savedDto = new ProductDto();

        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(category));
        when(productMapper.mapToProductEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.mapToProductDto(savedProduct)).thenReturn(savedDto);

        ProductDto result = productService.createProduct(dto);

        assertNotNull(result);
        verify(categoryRepository).findById(dto.getCategoryId());
        verify(productMapper).mapToProductEntity(dto);
        verify(productRepository).save(product);
        verify(productMapper).mapToProductDto(savedProduct);
    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        ProductDto dto = new ProductDto();
        dto.setCategoryId((byte) 1);

        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.createProduct(dto));
    }

    // -------------------- updateProduct --------------------
    @Test
    void testUpdateProduct_Success() {
        Long productId = 1L;
        ProductDto dto = new ProductDto();
        dto.setCategoryId((byte) 1);

        Product existingProduct = new Product();
        Product savedProduct = new Product();
        Category category = new Category();
        ProductDto updatedDto = new ProductDto();

        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productMapper).update(dto, existingProduct);
        when(productRepository.save(existingProduct)).thenReturn(savedProduct);
        when(productMapper.mapToProductDto(savedProduct)).thenReturn(updatedDto);

        ProductDto result = productService.updateProduct(productId, dto);

        assertNotNull(result);
        verify(categoryRepository).findById(dto.getCategoryId());
        verify(productRepository).findById(productId);
        verify(productMapper).update(dto, existingProduct);
        verify(productRepository).save(existingProduct);
        verify(productMapper).mapToProductDto(savedProduct);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Long productId = 1L;
        ProductDto dto = new ProductDto();
        dto.setCategoryId((byte) 1);

        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productId, dto));
    }

    @Test
    void testUpdateProduct_CategoryNotFound() {
        Long productId = 1L;
        ProductDto dto = new ProductDto();
        dto.setCategoryId((byte) 1);

        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(productId, dto));
    }

    // -------------------- deleteProduct --------------------
    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        assertDoesNotThrow(() -> productService.deleteProduct(productId));
        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
    }
}
