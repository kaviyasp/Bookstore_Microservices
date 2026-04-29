package com.bookstore.feedback.controller;

import com.bookstore.feedback.entity.Review;
import com.bookstore.feedback.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final ReviewRepository reviewRepo;

    @PostMapping
    public ResponseEntity<Review> submit(@RequestBody Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(reviewRepo.save(review));
    }

    @GetMapping("/product/{id}")
    public List<Review> getReviews(@PathVariable Long id) {
        return reviewRepo.findByProductId(id);
    }

    @GetMapping("/product/{id}/rating")
    public ResponseEntity<Double> getRating(@PathVariable Long id) {
        OptionalDouble avg = reviewRepo.findByProductId(id)
                .stream().mapToInt(Review::getRating).average();
        return ResponseEntity.ok(avg.orElse(0.0));
    }
}