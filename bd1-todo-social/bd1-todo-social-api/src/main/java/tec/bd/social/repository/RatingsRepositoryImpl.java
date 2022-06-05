package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.datasource.DBManager;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingsRepositoryImpl extends BaseRepository<Rating> implements RatingsRepository{

    private static final String FIND_BY_RATING_ID_QUERY = "{call find_rating(?)}";
    private static final String CALCULATE_AVG_PROC = "{call rating_avg(?)}";
    private static final String DELETE_RATING_TODO_ID = "{call delete_rating(?)}";

    public RatingsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }


    @Override
    public Rating findById(int ratingId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_BY_RATING_ID_QUERY);
            statement.setInt(1, ratingId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var rating = toEntity(resultSet);
                return rating;
            }
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
    public void deleteRating(String todoId) {
        try {
            var connect = this.connect();
            var statement = connect.prepareStatement(DELETE_RATING_TODO_ID);
            statement.setString(1, todoId);
            var actual = this.query(statement);
            System.out.println("Actual: " + actual);
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
