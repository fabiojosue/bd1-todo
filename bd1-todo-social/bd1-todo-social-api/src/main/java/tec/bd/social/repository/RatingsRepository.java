package tec.bd.social.repository;

import tec.bd.social.Rating;

public interface RatingsRepository {

    String findById(String clientId, String todoId);

    Rating createRating(Rating rating);

    float findAverage(String todoId);

    void deleteRating(String clientId, String todoId);

}
