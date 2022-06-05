package tec.bd.social.repository;

import tec.bd.social.Rating;

public interface RatingsRepository {

    Rating findById(int id);

    float findAverage(String todoId);

    void deleteRating(String todoId);
}
