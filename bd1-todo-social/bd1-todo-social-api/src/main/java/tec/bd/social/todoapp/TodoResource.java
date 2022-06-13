package tec.bd.social.todoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TodoResource {

    @GET("/api/v1/todos/:todo-id")
    Call<TodoRecord> validateInServer(@Query("todo-id") String todoId);

}
