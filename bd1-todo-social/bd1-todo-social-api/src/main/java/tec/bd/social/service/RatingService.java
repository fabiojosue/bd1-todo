package tec.bd.social.service;

import tec.bd.social.Rating;

public interface RatingService {

    Rating getRating(int ratingId);

    float getRatingAverage(String todoId);

    void deleteRating(String todoId);

    Rating newRating(Rating rating);

}
