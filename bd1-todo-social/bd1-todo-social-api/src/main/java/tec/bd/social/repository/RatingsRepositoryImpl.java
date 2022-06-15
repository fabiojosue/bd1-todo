package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.datasource.DBManager;

import java.sql.*;

public class RatingsRepositoryImpl extends BaseRepository<Rating> implements RatingsRepository{

    private static final String FIND_BY_RATING_ID_QUERY = "{call find_rating(?,?)}";
    private static final String CALCULATE_AVG_PROC = "{call rating_avg(?)}";
    private static final String DELETE_RATING_TODO_ID = "{call delete_rating(?,?)}";
    private static final String INSERT_NEW_RATING = "{call create_rating(?,?,?,?)}";

    public RatingsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }


    @Override
    public String findById(String clientId, String todoId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_BY_RATING_ID_QUERY);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var result = resultSet.getString("todoId");
                return result;
            }
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Rating createRating(Rating rating) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(INSERT_NEW_RATING);
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(rating.getCreatedAt().getTime());
            statement.setString(1, rating.getTodoId());
            statement.setTimestamp(2, sqlDate);
            statement.setInt(3, rating.getRatingValue());
            statement.setString(4, rating.getClientId());
            this.query(statement);
            return rating;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public float findAverage(String todoId) {
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(CALCULATE_AVG_PROC);
            statement.setString(1, todoId);

            var resultSet = this.query(statement);
            while(resultSet.next()){
                var ratingAvg = resultSet.getFloat("ratingAvg");
                return ratingAvg;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    @Override
    public void deleteRating(String clientId, String todoId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(DELETE_RATING_TODO_ID);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            this.query(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Rating toEntity(ResultSet resultSet) {
        try {
            var client = new Rating(
                    resultSet.getInt("id"),
                    resultSet.getInt("calification"),
                    resultSet.getString("todoId"),
                    resultSet.getString("userId"),
                    resultSet.getTimestamp("createdAt")
            );

            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
