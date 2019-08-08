package com.udacity.course3.reviews;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDocument;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewDocumentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureDataMongo
public class ReviewsApplicationTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewDocumentRepository reviewDocumentRepository;

    @Test
    public void is_not_null_test() {
        assertThat(commentRepository).isNotNull();
        assertThat(reviewRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    @Test
    public void create_product_test() {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        productRepository.save(product);
        assertThat(productRepository.findById(product.getId())).isNotNull();
    }

    @Test
    public void get_products_list_test() {
        Product product = new Product();
        product.setName("Product Name");
        product.setDescription("Product Description");
        productRepository.save(product);

        List<String> prouducts = new ArrayList<>();
        for (Product p : productRepository.findAll())
            prouducts.add(p.getName());
        assertThat(prouducts).isNotEmpty();
    }

    @Test
    public void save_review_with_product_test() {
        Product product = new Product();
        product.setName("Product Name");
        product.setDescription("Product Description");
        productRepository.save(product);
        Optional<Product> productOptional = productRepository.findById(product.getId());

        Review review = new Review();
        review.setProduct(productOptional.get());
        review.setTitle("Text title");
        review.setText("Text");
        reviewRepository.save(review);

        assertThat(reviewRepository.findAllByProductId(product.getId())).isNotEmpty();
    }

    @Test
    public void save_review_with_comment_test() {
        Product product = new Product();
        product.setName("Product name");
        product.setDescription("Product description");

        productRepository.save(product);
        Optional<Product> o_product = productRepository.findById(product.getId());

        Review review = new Review();
        review.setProduct(o_product.get());
        review.setTitle("Title");
        review.setText("Text");
        reviewRepository.save(review);
        Optional<Review> o_review = reviewRepository.findById(review.getId());

        Comment comment = new Comment();
        comment.setReview(o_review.get());
        comment.setText("Comment");
        comment.setTitle("Title");
        commentRepository.save(comment);

        assertThat(commentRepository.findAllByReviewId(review.getId())).isNotEmpty();
    }

    @Test
    public void create_review_document_test() {
        Product product = new Product();
        product.setName("Product name");
        product.setDescription("Product description");
        product = productRepository.save(product);
        Review review = new Review();
        review.setProduct(product);
        review.setTitle("Title");
        review.setText("Text");
        review = reviewRepository.save(review);
        ReviewDocument reviewDocument = new ReviewDocument();
        reviewDocument.setId(review.getId());
        reviewDocument.setReview(review);
        reviewDocumentRepository.save(reviewDocument);
        assertThat(reviewDocumentRepository.findById(review.getId())).isNotNull();
    }

}