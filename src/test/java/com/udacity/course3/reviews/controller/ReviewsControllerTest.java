package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ReviewsControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<Review> json;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() {
        reviewRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void createReviewForProduct() throws Exception {
        Product product = productRepository.save(getProduct());
        Review review = getReview();
        mvc.perform(
                post(new URI("/reviews/products/" + product.getId()))
                        .content(json.write(review).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void listReviewsForProduct() throws Exception {
        Product product = productRepository.save(getProduct());
        Review review1 = getReview();
        Review review2 = getReview();
        review1.setProduct(product);
        review2.setProduct(product);
        reviewRepository.saveAll(Arrays.asList(review1, review2));
        mvc.perform(
                get(new URI("/reviews/products/" + product.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private Review getReview() {
        Review review = new Review();
        review.setTitle("title");
        review.setText("text");
        return review;
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName("name");
        product.setDescription("description");
        return product;
    }
}