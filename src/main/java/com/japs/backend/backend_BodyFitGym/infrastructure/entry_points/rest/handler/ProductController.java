package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.ProductService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Product>> save(@Valid @RequestBody Product productRequest) {

        log.info("[ProductController] POST /save - Creando producto: {}", productRequest.getName());

        Product product = productService.save(productRequest);
        ApiResponse<Product> apiResponse = ResponseBuilder.successMessage("Producto registrado exitosamente", product);

        log.info("[ProductController] POST /save - Producto creado con ID: {}", product.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Product>> update(@PathVariable Long id, @Valid @RequestBody Product productRequest) {

        log.info("[ProductController] PUT /update/{} - Actualizando producto", id);

        Product product = productService.update(id, productRequest);
        ApiResponse<Product> apiResponse = ResponseBuilder.successMessage("Producto actualizado exitosamente", product);

        log.info("[ProductController] PUT /update/{} - Producto actualizado", id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("[ProductController] DELETE /delete/{} - Eliminando producto", id);

        productService.delete(id);
        ApiResponse<Void> apiResponse = ResponseBuilder.successMessage("Producto eliminado con éxito");

        log.info("[ProductController] DELETE /delete/{} - Producto eliminado correctamente", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Long id) {

        log.info("[ProductController] GET /find-by-id/{} - Buscando producto", id);

        Product product = productService.findById(id);
        ApiResponse<Product> apiResponse = ResponseBuilder.successMessage("Producto encontrado", product);

        log.info("[ProductController] GET /find-by-id/{} - Producto encontrado: {}", id, product.getName());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Product>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[ProductController] GET /search - Filtros: name={}, status={}", name, status);

        ProductSearchCriteria productSearchCriteria = ProductSearchCriteria.builder()
                .name(name)
                .status(status)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productService.search(productSearchCriteria, pageable);

        ApiResponse<Page<Product>> apiResponse = ResponseBuilder.successMessage("Productos encontrados", productPage);

        log.info("[ProductController] GET /search - Resultados encontrados: {}", productPage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
