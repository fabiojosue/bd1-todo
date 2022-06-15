package tec.bd.social.repository;

import tec.bd.social.Image;
import tec.bd.social.Rating;
import tec.bd.social.Review;
import tec.bd.social.datasource.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReviewsRepositoryImpl extends BaseRepository<Review> implements ReviewsRepository{

    private final static String CREATE_NEW_REVIEW = "{call create_review(?,?,?,?)}";
    private final static String CREATE_NEW_IMAGE = "{call create_image(?,?,?,?)}";
    private final static String UPDATE_REVIEW = "{call update_review(?,?,?,?)}";
    private final static String FIND_REVIEW_USER_TODO = "{call find_review(?,?)}";
    private final static String FIND_REVIEWS_FROM_TODOID = "{call get_reviews(?)}";
    private final static String DELETE_REVIEW_USER_TODO = "{call delete_review(?,?)}";
    private final static String COUNT_IMAGES_TODO_USER = "{call count_images(?,?)}";
    private final static String GET_IMG_FROM_REVIEW = "{call get_img_review(?,?)}";

    public ReviewsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }

    @Override
    public Review toEntity(ResultSet resultSet) {
        return null;
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
    public String find(String clientId, String todoId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_REVIEW_USER_TODO);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                //var review = toEntity(resultSet);
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
    public void delete(String clientId, String todoId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(DELETE_REVIEW_USER_TODO);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            this.query(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countImages(String clientId, String todoId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(COUNT_IMAGES_TODO_USER);
            statement.setString(1, todoId);
            statement.setString(2, clientId);
            var resultSet = this.query(statement);
            var total = resultSet.getInt("totalImg");
            return total;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Image newImage(Image image) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(CREATE_NEW_IMAGE);
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(image.getCreatedAt().getTime());
            statement.setString(1, image.getTodoId());
            statement.setTimestamp(2, sqlDate);
            statement.setString(3, image.getUrl());
            statement.setString(4, image.getClientId());
            this.query(statement);
            return image;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Object> getReviews(String clientId, String todoId) {
        ArrayList<Object> reviews = new ArrayList<Object>();
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_REVIEWS_FROM_TODOID);
            statement.setString(1, todoId);
            //statement.setString(2, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var imgs = this.getImgReview(resultSet.getString("userId"), todoId);
                reviews.add(toEntity2(resultSet, imgs));

//                reviews.add(imgs);
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<String> getImgReview(String clientId, String todoId) {
        ArrayList<String> images = new ArrayList<String>();
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(GET_IMG_FROM_REVIEW);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                images.add(resultSet.getString("url"));
            }
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Review toEntity2(ResultSet resultSet, ArrayList<String> imgs) {
        try {
            if (imgs.get(0) != null){
                var review = new Review(
                        resultSet.getInt("id"),
                        resultSet.getString("reviewText"),
                        resultSet.getString("todoId"),
                        resultSet.getString("userId"),
                        resultSet.getTimestamp("createdAt"),
                        imgs
                );
                return review;
            }
            var review = new Review(
                    resultSet.getInt("id"),
                    resultSet.getString("reviewText"),
                    resultSet.getString("todoId"),
                    resultSet.getString("userId"),
                    resultSet.getTimestamp("createdAt"),
                    new ArrayList<String>()
            );


            return review;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
