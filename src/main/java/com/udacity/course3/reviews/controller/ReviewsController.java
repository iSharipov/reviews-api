package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDocument;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewDocumentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewDocumentRepository reviewDocumentRepository;

    public ReviewsController(ReviewRepository reviewRepository, ProductRepository productRepository, ReviewDocumentRepository reviewDocumentRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.reviewDocumentRepository = reviewDocumentRepository;
    }

    /**
     * Creates a review for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<Review> createReviewForProduct(@PathVariable("productId") Integer productId, @RequestBody Review review) {
        if (isEmpty(review.getTitle())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            review.setProduct(productOptional.get());
            review = reviewRepository.save(review);
            ReviewDocument reviewDocument = new ReviewDocument();
            reviewDocument.setId(review.getId());
            reviewDocument.setReview(review);
            reviewDocumentRepository.save(reviewDocument);
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReviewDocument>> listReviewsForProduct(@PathVariable("productId") Integer productId) {
        Optional<List<Review>> reviewsOptional = reviewRepository.findAllByProductId(productId);
        if (reviewsOptional.isPresent()) {
            List<ReviewDocument> reviewDocuments = new ArrayList<>();
            for (Review review : reviewsOptional.get()) {
                reviewDocuments.add(reviewDocumentRepository.findById(review.getId()));
            }
            return ResponseEntity.ok(reviewDocuments);
        }
        return ResponseEntity.notFound().build();
    }
}