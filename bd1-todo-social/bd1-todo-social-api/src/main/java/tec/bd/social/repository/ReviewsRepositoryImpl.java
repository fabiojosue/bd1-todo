package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.Review;
import tec.bd.social.datasource.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReviewsRepositoryImpl extends BaseRepository<Review> implements ReviewsRepository{

    private final static String FIND_REVIEWS_FROM_TODOID = "{call get_reviews(?)}";
    private final static String CREATE_NEW_REVIEW = "{call create_review(?,?,?,?)}";
    private final static String UPDATE_REVIEW = "{call update_review(?,?,?,?)}";

    public ReviewsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }

    @Override
    public Review newReview(Review review) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(CREATE_NEW_REVIEW);
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(review.getCreatedAt().getTime());
            statement.setString(1, review.getTodoId());
            statement.setTimestamp(2, sqlDate);
            statement.setString(3, review.getReviewText());
            statement.setString(4, review.getClientId());
            this.query(statement);
            return review;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Review update(Review review) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(UPDATE_REVIEW);
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(review.getCreatedAt().getTime());
            statement.setString(1, review.getClientId());
            statement.setString(2, review.getReviewText());
            statement.setString(3, review.getTodoId());
            statement.setTimestamp(4, sqlDate);
            this.query(statement);
            return review;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Review> getReviews(String todoId) {
        ArrayList<Review> reviews = new ArrayList<Review>();
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_REVIEWS_FROM_TODOID);
            statement.setString(1, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                //var review = toEntity(resultSet);
                reviews.add(toEntity(resultSet));
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Review toEntity(ResultSet resultSet) {
        try {
            var review = new Review(
                    resultSet.getInt("id"),
                    resultSet.getString("reviewText"),
                    resultSet.getString("todoId"),
                    resultSet.getString("userId"),
                    resultSet.getTimestamp("createdAt")
            );

            return review;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
