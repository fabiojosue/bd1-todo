package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.Review;
import tec.bd.social.datasource.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReviewsRepositoryImpl extends BaseRepository<Review> implements ReviewsRepository{

    private final static String FIND_REVIEWS_FROM_TODOID = "{call get_reviews(?)}";

    public ReviewsRepositoryImpl(DBManager dbManager){
        super(dbManager);
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
