package tec.bd.social.repository;

import tec.bd.social.Review;

import java.util.ArrayList;

public interface ReviewsRepository {

    ArrayList<Review> getReviews(String todoId);

}
