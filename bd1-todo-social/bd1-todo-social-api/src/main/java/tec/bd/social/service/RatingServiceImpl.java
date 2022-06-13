package tec.bd.social.service;

import tec.bd.social.Rating;
import tec.bd.social.repository.RatingsRepository;

import java.util.Date;

public class RatingServiceImpl implements  RatingService{

    private RatingsRepository ratingsRepository;

    public RatingServiceImpl(RatingsRepository ratingsRepository){
        this.ratingsRepository = ratingsRepository;
    }

    @Override
    public Rating getRating(int ratingId) {
        return this.ratingsRepository.findById(ratingId);
    }

    @Override
    public float getRatingAverage(String todoId) {
        return this.ratingsRepository.findAverage(todoId);
    }

    @Override
    public void deleteRating(String todoId) {
        this.ratingsRepository.deleteRating(todoId);
    }

    @Override
    public Rating newRating(Rating rating) {
//        return ratingsRepository.createRating(
//                new Rating(1, rating.getRatingValue(), rating.getTodoId(), rating.getClientId(), new Date())
//        );
        return this.ratingsRepository.createRating(rating);
    }
}
