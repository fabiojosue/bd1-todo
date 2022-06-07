package tec.bd.social.service;

import java.util.ArrayList;

import tec.bd.social.Review;

public interface ReviewsService {
    ArrayList<Review> getReviews(String todoId);
}
