package tec.bd.social.service;

import java.util.ArrayList;

import tec.bd.social.Image;
import tec.bd.social.Review;

public interface ReviewsService {
    Review createReview(Review review);

    Image createImg(Image image);

    Review updateReview(Review review);

    String findReview(String clientId, String todoId);

    void deleteReview(String clientId, String todoId);

    int countImg(String clientId, String todoId);

    ArrayList<Object> getReviews(String clientId, String todoId);
}
