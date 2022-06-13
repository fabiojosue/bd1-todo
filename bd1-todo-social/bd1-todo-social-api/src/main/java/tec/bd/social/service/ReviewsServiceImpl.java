package tec.bd.social.service;

import tec.bd.social.repository.ReviewsRepository;

import java.util.ArrayList;
import tec.bd.social.Review;

public class ReviewsServiceImpl implements ReviewsService{

    private ReviewsRepository reviewsRepository;

    public ReviewsServiceImpl(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public Review createReview(Review review) {
        return this.reviewsRepository.newReview(review);
    }

    @Override
    public Review updateReview(Review review) {
        return this.reviewsRepository.update(review);
    }

    @Override
    public ArrayList<Review> getReviews(String todoId) {
        return this.reviewsRepository.getReviews(todoId);
    }
}
