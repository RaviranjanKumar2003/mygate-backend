package com.example.demo.ECommerce.EControllers;

import com.example.demo.ECommerce.Dtos.ProductDto;
import com.example.demo.ECommerce.ERepositories.ProductRepository;
import com.example.demo.ECommerce.EServices.ProductService;
import com.example.demo.ECommerce.Eentities.Product;
import com.example.demo.Services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Value("${project.image}")
    private String path;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductRepository productRepository;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestParam Long userId,   // ✅ ADD THIS
            @RequestBody ProductDto dto
    ) {
        ProductDto updated = productService.updateProduct(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id,
            @RequestParam Long userId   // ✅ frontend se aayega
    ) {
        productService.deleteProduct(id, userId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<ProductDto>> getProductsBySociety(@PathVariable Long societyId) {
        return ResponseEntity.ok(productService.getProductsBySociety(societyId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDto>> getProductsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }




//===================================================== IMAGE ===========================================
@PostMapping("/society/{societyId}/product/image/upload/{productId}")
public ResponseEntity<ProductDto> uploadProductImage(
        @RequestParam("images") List<MultipartFile> images,
        @PathVariable Long productId,
        @PathVariable Long societyId
) throws IOException {

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    List<String> imageList = new ArrayList<>();

    // ✅ OLD IMAGES KEEP
    if (product.getImages() != null) {
        imageList.addAll(product.getImages());
    }

    // ✅ ADD NEW IMAGES
    for (MultipartFile file : images) {
        String fileName = fileService.uploadImage(path, file);
        imageList.add(fileName);
    }

    product.setImages(imageList);
    productRepository.save(product);

    return ResponseEntity.ok(productService.getProductById(productId));
}

// GET PRODUCT IMAGE
    @GetMapping("/image/get/product/{productId}/{imageName}")
    public void downloadProductImage(
            @PathVariable Long productId,
            @PathVariable String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = fileService.getResource(path, imageName);

        String contentType = URLConnection.guessContentTypeFromName(imageName);
        response.setContentType(
                contentType != null ? contentType : "application/octet-stream"
        );

        StreamUtils.copy(resource, response.getOutputStream());
    }



}