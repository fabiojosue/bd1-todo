package tec.bd.social.todoapp;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TodoResource {

    @GET("api/v1/todos/{todo-id}")//path parameter
    Call<TodoRecord> validateInServer(@Header("x-session-id") String sessionId, @Path("todo-id") String todoId);


}
