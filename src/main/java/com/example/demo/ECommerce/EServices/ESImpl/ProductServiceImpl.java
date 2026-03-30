package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.ProductDto;
import com.example.demo.ECommerce.ERepositories.ProductRepository;
import com.example.demo.ECommerce.EServices.ProductService;
import com.example.demo.ECommerce.Eentities.Product;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private UserRepository userRepository;

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Mapping Product -> ProductDto
    private ProductDto mapToDto(Product product) {

        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setImages(product.getImages());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());

        // ✅ CORRECT (since you use IDs)
        dto.setSellerId(product.getSellerId());
        dto.setSocietyId(product.getSocietyId());

        dto.setCodAvailable(
                product.getCodAvailable() != null ? product.getCodAvailable() : true
        );

        return dto;
    }

    // Mapping ProductDto -> Product
    private Product mapToEntity(ProductDto dto) {

        Product product = new Product();

        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setImages(dto.getImages());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());

        // ✅ DIRECT ID SET
        product.setSellerId(dto.getSellerId());
        product.setSocietyId(dto.getSocietyId());

        product.setCodAvailable(
                dto.getCodAvailable() != null ? dto.getCodAvailable() : true
        );

        return product;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    public ProductDto updateProduct(Long id, ProductDto dto, Long userId) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ AUTH CHECK
        if (!product.getSellerId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this product");
        }

        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());

        Product updated = productRepository.save(product);
        return mapToDto(updated);
    }

    @Override
    public void deleteProduct(Long id, Long userId) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ AUTH CHECK
        if (!product.getSellerId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsBySociety(Long societyId) {
        return productRepository.findBySocietyId(societyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String uploadDir = "uploads/product-images/";

        // ✅ Delete old images (optional)
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (String img : product.getImages()) {
                deleteFile(uploadDir, img);
            }
        }

        // ✅ Save new image
        String fileName = saveFile(image, uploadDir);

        // ✅ Set new image list
        List<String> images = new ArrayList<>();
        images.add(fileName);
        product.setImages(images);

        productRepository.save(product);

        return mapToDto(product);
    }

    private String saveFile(MultipartFile file, String uploadDir) {

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String originalName = file.getOriginalFilename();

            if (originalName == null) {
                originalName = "image.jpg";
            }

            // ✅ Validate file type
            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Only image files allowed");
            }

            // ✅ File size limit (2MB)
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new RuntimeException("File size should be less than 2MB");
            }

            String fileName = UUID.randomUUID() + "_" + originalName;
            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath, file.getBytes());

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Image save failed");
        }
    }

    private void deleteFile(String path, String fileName) {
        try {
            Path filePath = Paths.get(path + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("File delete failed: " + fileName);
        }
    }


}