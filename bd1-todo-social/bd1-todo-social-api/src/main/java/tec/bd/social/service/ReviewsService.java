package tec.bd.social.service;

import java.util.ArrayList;

import tec.bd.social.Review;

public interface ReviewsService {
    Review createReview(Review review);

    Review updateReview(Review review);

    ArrayList<Review> getReviews(String todoId);
}
