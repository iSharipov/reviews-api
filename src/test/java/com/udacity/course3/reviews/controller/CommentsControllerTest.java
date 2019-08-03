package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repository.CommentRepository;
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
public class CommentsControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<Comment> json;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() {
        commentRepository.deleteAll();
        reviewRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void createCommentForReview() throws Exception {
        Product product = productRepository.save(getProduct());
        Review review = getReview();
        review.setProduct(product);
        review = reviewRepository.save(review);
        Comment comment = getComment();
        mvc.perform(
                post(new URI("/comments/reviews/" + review.getId()))
                        .content(json.write(comment).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void listCommentsForReview() throws Exception {
        Product product = productRepository.save(getProduct());
        Review review = getReview();
        review.setProduct(product);
        review = reviewRepository.save(review);
        Comment comment1 = getComment();
        Comment comment2 = getComment();
        comment1.setReview(review);
        comment2.setReview(review);
        commentRepository.saveAll(Arrays.asList(comment1, comment2));
        mvc.perform(
                get(new URI("/comments/reviews/" + review.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName("name");
        product.setDescription("description");
        return product;
    }

    private Comment getComment() {
        Comment comment = new Comment();
        comment.setTitle("title");
        comment.setText("text");
        return comment;
    }

    private Review getReview() {
        Review review = new Review();
        review.setTitle("title");
        review.setText("text");
        return review;
    }
}
