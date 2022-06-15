package tec.bd.social.service;

import tec.bd.social.Rating;

public interface RatingService {

    String getRating(String clientId, String todoId);

    float getRatingAverage(String todoId);

    void deleteRating(String clientId, String todoId);

    Rating newRating(Rating rating);

}
