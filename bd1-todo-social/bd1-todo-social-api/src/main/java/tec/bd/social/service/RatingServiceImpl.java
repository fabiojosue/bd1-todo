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
    public String getRating(String clientId, String todoId) {
        return this.ratingsRepository.findById(clientId, todoId);
    }

    @Override
    public float getRatingAverage(String todoId) {
        return this.ratingsRepository.findAverage(todoId);
    }

    @Override
    public void deleteRating(String clientId, String todoId) {
        this.ratingsRepository.deleteRating(clientId, todoId);
    }

    @Override
    public Rating newRating(Rating rating) {
        return this.ratingsRepository.createRating(rating);
    }
}
