package tec.bd.social;

import com.google.gson.Gson;
import tec.bd.social.authentication.SessionStatus;
import tec.bd.social.todoapp.Status;
import tec.bd.social.todoapp.TodoRecord;

import java.util.Map;

import static spark.Spark.*;
/**
 * Hello world!
 *
 */
public class SocialApi
{
    public static void main( String[] args )
    {
        WebApplicationContext context = WebApplicationContext.init();
        var authenticationClient = context.getAuthenticationClient();
        var todoAuthentication = context.getTodoAuthentication();
        var ratingsService = context.getRatingService();
        var reviewsService = context.getReviewsService();
        Gson gson = context.getGson();

        port(8082);

        //Autentication
        before((request, response) -> {

            var sessionParam = request.headers("x-session-id");
            if(null == sessionParam) {
                halt(401, "Unauthorized");
            }
            var session = authenticationClient.validateSession(sessionParam);
            if(session.getStatus() == SessionStatus.INACTIVE) {
                halt(401, "Unauthorized");
            }
        });

        options("/", (request, response) -> {
            response.header("Content-Type", "application/json");
            return Map.of(
                    ".Message", "SOCIAL API V1",
                    "POST ratings/:todo-id","Crear un nuevo Rating a un todoId con el user del session",
                    "POST reviews/:todo-id","Crear un nuevo review a un todoId con el user del session",
                    "POST reviews/:todo-id/images","Crear una nueva imagen a un todoId con el user del session",
                    "GET todos/:todo-id/rating","Obtiene el valor promedio de los ratings de un todoId",
                    "GET reviews/:todo-id","Obtener todos los reviews de un todoId",
                    "DELETE /ratings/:todo-id","Borrar un rating a un todoId con el user del session",
                    "DELETE /reviews/:todo-id","Borrar el review a un todoId con el user del session",
                    "PUT reviews/:todo-id","Editar un review existente a un todoId con el user del session"
            );
        }, gson::toJson);

        // Crear un nuevo Rating a un todoId con el user del session
        post("ratings/:todo-id", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var todo = todoAuthentication.validateTodo(sessionParam,todoParam);
            if (null == todo || todo.getStatus() == Status.BLOCKED){
                halt(404, "Todo Not Found");
            }
            var session = authenticationClient.validateSession(sessionParam);
            var ratingParams = gson.fromJson(request.body(), Rating.class);
            ratingParams.setTodoId(todoParam);
            ratingParams.setClientId(session.getClientId());
            try {
                var rating = ratingsService.newRating(ratingParams);
                response.status(200);
                return rating;
            } catch (Exception e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        }, gson::toJson);

        // Obtiene el valor promedio de los ratings de un todoId
        get("todos/:todo-id/rating", (request, response) -> {
            var todoId = request.params("todo-id");

            var ratingAvg = ratingsService.getRatingAverage(todoId);
            response.status(200);

            response.header("Content-Type", "application/json");
            return Map.of(
                    "todo-id", todoId,
                    "rating", ratingAvg
                    );
        }, gson::toJson);

        // Borrar un rating a un todoId con el user del session
        delete("/ratings/:todo-id", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var session = authenticationClient.validateSession(sessionParam);
            var validateRating = ratingsService.getRating(session.getClientId(),todoParam);
            if (validateRating != "" ) {
                ratingsService.deleteRating(session.getClientId(),todoParam);
                response.status(200);
                return Map.of("Deleted", "OK");
            }
            response.status(404);
            return Map.of("Review","Not found");
        }, gson::toJson);

        // Obtener todos los reviews de un todoId
        get("reviews/:todo-id", (request, response) -> {
            var todoParam = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var session = authenticationClient.validateSession(sessionParam);
            var review = reviewsService.getReviews(session.getClientId(),todoParam);

            if (null != review){
                return review;
            }

            response.status(404);
            return Map.of();
        }, gson::toJson);

        //Crear un nuevo review a un todoId con el user del session
        post("reviews/:todo-id", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var todo = todoAuthentication.validateTodo(sessionParam,todoParam);
            if (null == todo || todo.getStatus() == Status.BLOCKED){
                halt(404, "Todo Not Found");
            }
            var session = authenticationClient.validateSession(sessionParam);
            var reviewParams = gson.fromJson(request.body(), Review.class);
            reviewParams.setTodoId(todoParam);
            reviewParams.setClientId(session.getClientId());
            try {
                var validateReview = reviewsService.findReview(session.getClientId(),todoParam);
                if (validateReview == "" ){
                    var review = reviewsService.createReview(reviewParams);
                    if (null != review){
                        response.status(200);
                        return review;
                    }
                }
                response.status(404);
                return Map.of();
            } catch (Exception e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        }, gson::toJson);

        // Borrar el review a un todoId con el user del session
        delete("/reviews/:todo-id", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var session = authenticationClient.validateSession(sessionParam);
            var validateReview = reviewsService.findReview(session.getClientId(),todoParam);
            if (validateReview != "" ) {
                reviewsService.deleteReview(session.getClientId(),todoParam);
                response.status(200);
                return Map.of("Deleted", "OK");
            }
            response.status(404);
            return Map.of("Review","Not found");
        }, gson::toJson);

        // Editar un review existente a un todoId con el user del session
        put("reviews/:todo-id", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var todo = todoAuthentication.validateTodo(sessionParam,todoParam);
            if (null == todo || todo.getStatus() == Status.BLOCKED){
                halt(404, "Todo Not Found");
            }
            var session = authenticationClient.validateSession(sessionParam);
            var reviewParams = gson.fromJson(request.body(), Review.class);
            reviewParams.setTodoId(todoParam);
            reviewParams.setClientId(session.getClientId());
            try {
                var review = reviewsService.updateReview(reviewParams);
                response.status(200);
                if (null != review){
                    return review;
                }
                response.status(404);
                return Map.of();
            } catch (Exception e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        }, gson::toJson);

        // Crear una nueva imagen a un todoId con el user del session
        post("reviews/:todo-id/images", (request, response) -> {
            var sessionParam = request.headers("x-session-id");
            var todoParam = request.params("todo-id");
            var todo = todoAuthentication.validateTodo(sessionParam,todoParam);
            if (null == todo || todo.getStatus() == Status.BLOCKED){
                halt(404, "Todo Not Found");
            }
            var session = authenticationClient.validateSession(sessionParam);
            var imgParams = gson.fromJson(request.body(), Image.class);
            imgParams.setTodoId(todoParam);
            imgParams.setClientId(session.getClientId());
            try {
                var validateReview = reviewsService.findReview(session.getClientId(),todoParam);
                if (validateReview != "" ){
                    var imgNo = reviewsService.countImg(session.getClientId(),todoParam);
                    if (imgNo <= 2) {
                        var image = reviewsService.createImg(imgParams);
                        response.status(200);
                        return image;
                    }
                }
                response.status(409);
                return Map.of("Message","Review has max amount of images");
            } catch (Exception e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        }, gson::toJson);

    }
}
