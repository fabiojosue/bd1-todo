package tec.bd.social.repository;

import tec.bd.social.Image;
import tec.bd.social.Review;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface ReviewsRepository {

    Image newImage(Image image);

    Review newReview(Review review);

    Review update(Review review);

    String find(String clientId, String todoId);

    void delete(String clientId, String todoId);

    int countImages(String clientId, String todoId);

    ArrayList<Object> getReviews(String clientId, String todoId);

    ArrayList<String> getImgReview(String clientId, String todoId);

    Review toEntity2(ResultSet resultSet, ArrayList<String> imgs);

}
