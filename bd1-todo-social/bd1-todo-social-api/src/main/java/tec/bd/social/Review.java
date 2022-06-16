package tec.bd.social;

import java.util.ArrayList;
import java.util.Date;

public class Review {

    private int reviewId;
    private String reviewText;
    private String todoId;
    private String clientId;
    private Date createdAt;
    private ArrayList<String> images;

    public Review(int reviewId, String reviewText, String todoId, String clientId, Date createdAt, ArrayList<String> images) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.todoId = todoId;
        this.clientId = clientId;
        this.createdAt = createdAt;
        this.images = images;
    }

    public ArrayList<String> getImgs() {
        return images;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.images = imgs;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
