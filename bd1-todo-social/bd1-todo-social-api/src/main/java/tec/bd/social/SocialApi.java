package tec.bd.social;

import com.google.gson.Gson;
import tec.bd.social.authentication.SessionStatus;

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
                    "message", "SOCIAL API V1");
        }, gson::toJson);

        //
        get("ratings/:rating-id", (request, response) -> {
            var ratingIdParam = request.params("rating-id");
            var ratingId = Integer.parseInt(ratingIdParam);

            var rating = ratingsService.getRating(ratingId);

            if (null != rating){
                return rating;
            }

            response.status(404);
            return Map.of();
//            response.header("Content-Type", "application/json");
//            return Map.of(
//                    "message", "Get rating for todo-id: " + ratingId);
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

        // Borrar todos los ratings de un todoId
        delete("/ratings/:todo-id", (request, response) -> {
            var todoId = request.params("todo-id");
            ratingsService.deleteRating(todoId);
            response.status(200);
            return Map.of("Deleted", "OK");
        }, gson::toJson);

        // Obtener todos los reviews de un todoId
        get("reviews/:todo-id", (request, response) -> {
            var todoId = request.params("todo-id");

            var review = reviewsService.getReviews(todoId);

            if (null != review){
                return review;
            }

            response.status(404);
            return Map.of();
        }, gson::toJson);

    }
}
