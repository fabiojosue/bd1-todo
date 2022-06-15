package tec.bd.social;

import java.util.Date;

public class Image {
    private String todoId;
    private String clientId;
    private String url;
    private Date createdAt;

    public Image(String todoId, String clientId, String url, Date createdAt) {
        this.todoId = todoId;
        this.clientId = clientId;
        this.url = url;
        this.createdAt = createdAt;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}


