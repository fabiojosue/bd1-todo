package tec.bd.social.service;

import tec.bd.social.Image;
import tec.bd.social.repository.ReviewsRepository;

import java.util.ArrayList;
import tec.bd.social.Review;

public class ReviewsServiceImpl implements ReviewsService{

    private ReviewsRepository reviewsRepository;

    public ReviewsServiceImpl(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public Review createReview(Review review) {
        return this.reviewsRepository.newReview(review);
    }

    @Override
    public Image createImg(Image image) {
        return this.reviewsRepository.newImage(image);
    }

    @Override
    public Review updateReview(Review review) {
        return this.reviewsRepository.update(review);
    }

    @Override
    public String findReview(String clientId, String todoId) {
        return this.reviewsRepository.find(clientId, todoId);
    }

    @Override
    public void deleteReview(String clientId, String todoId) {
        this.reviewsRepository.delete(clientId, todoId);
    }

    @Override
    public int countImg(String clientId, String todoId) {
        return this.reviewsRepository.countImages(clientId, todoId);
    }

    @Override
    public ArrayList<Object> getReviews(String clientId, String todoId) {
        return this.reviewsRepository.getReviews(clientId,todoId);
    }
}
