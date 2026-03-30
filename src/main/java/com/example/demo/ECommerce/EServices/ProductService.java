package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto dto, Long userId);

    void deleteProduct(Long id, Long userId);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsBySociety(Long societyId);

    List<ProductDto> getProductsBySeller(Long sellerId);

    ProductDto updateProductImage(Long productId, MultipartFile image);


}