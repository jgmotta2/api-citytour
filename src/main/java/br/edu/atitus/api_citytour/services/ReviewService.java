package br.edu.atitus.api_citytour.services;

import br.edu.atitus.api_citytour.entities.PointEntity;
import br.edu.atitus.api_citytour.entities.ReviewEntity;
import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.repositories.PointRepository;
import br.edu.atitus.api_citytour.repositories.ReviewRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;

    public ReviewService(ReviewRepository reviewRepository, PointRepository pointRepository) {
        this.reviewRepository = reviewRepository;
        this.pointRepository = pointRepository;
    }

    public ReviewEntity saveReview(UUID placeId, ReviewEntity review) throws Exception {
        if (review == null) {
            throw new Exception("Review object cannot be null.");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new Exception("The rating must be between 1 and 5.");
        }

        PointEntity place = pointRepository.findById(placeId)
                .orElseThrow(() -> new Exception("Place with ID " + placeId + " not found."));

        // Get the logged-in user from the security context
        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        review.setPlace(place);
        review.setUser(userAuth);
        review.setReviewDate(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<ReviewEntity> getReviewsByPlace(UUID placeId) throws Exception {
        PointEntity place = pointRepository.findById(placeId)
                .orElseThrow(() -> new Exception("Place with ID " + placeId + " not found."));
        return reviewRepository.findByPlace(place);
    }

    // Method to calculate the average rating for a place (can be useful)
    public double getAverageRatingForPlace(UUID placeId) {
        List<ReviewEntity> reviews = reviewRepository.findByPlace(pointRepository.getReferenceById(placeId));
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream().mapToInt(ReviewEntity::getRating).average().orElse(0.0);
    }

    // For "Top Rated"
    public List<ReviewEntity> getTopRatedReviews(int minRating) {
        return reviewRepository.findByRatingGreaterThanEqualOrderByRatingDesc(minRating);
    }

    public Optional<ReviewEntity> findById(UUID id) {
        return reviewRepository.findById(id);
    }
}
