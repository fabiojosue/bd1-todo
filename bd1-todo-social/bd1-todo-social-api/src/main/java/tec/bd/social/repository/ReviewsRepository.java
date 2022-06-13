package tec.bd.social.repository;

import tec.bd.social.Review;

import java.util.ArrayList;

public interface ReviewsRepository {

    Review newReview(Review review);

    Review update(Review review);

    ArrayList<Review> getReviews(String todoId);

}
