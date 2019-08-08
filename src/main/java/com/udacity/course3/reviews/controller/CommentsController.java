package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDocument;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewDocumentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    // TODO: Wire needed JPA repositories here
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final ReviewDocumentRepository reviewDocumentRepository;

    public CommentsController(ReviewRepository reviewRepository, CommentRepository commentRepository, ReviewDocumentRepository reviewDocumentRepository) {
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.reviewDocumentRepository = reviewDocumentRepository;
    }

    /**
     * Creates a comment for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<Comment> createCommentForReview(@PathVariable("reviewId") Integer reviewId, @RequestBody Comment comment) {
        final Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            if (!isEmpty(comment.getTitle())) {
                comment.setReview(reviewOptional.get());
                commentRepository.save(comment);
                ReviewDocument reviewDocument = reviewDocumentRepository.findById(reviewOptional.get().getId());
                if (reviewDocument != null) {
                    reviewDocument.getComments().add(comment);
                    reviewDocumentRepository.save(reviewDocument);
                }
                return ResponseEntity.ok(comment);
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * List comments for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Optional<List<Comment>> commentsOptional = commentRepository.findAllByReviewId(reviewId);
        if (commentsOptional.isPresent()) {
            return ResponseEntity.ok(commentsOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}