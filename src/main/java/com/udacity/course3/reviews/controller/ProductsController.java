package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Spring REST controller for working with product entity.
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    // TODO: Wire JPA repositories here
    private final ProductRepository productRepository;

    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a product.
     *
     * @param product product
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (isEmpty(product.getName()) || isEmpty(product.getDescription())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.ok(productRepository.save(product));
    }

    /**
     * Finds a product by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @RequestMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<?> listProducts() {
        return productRepository.findAll();
    }
}