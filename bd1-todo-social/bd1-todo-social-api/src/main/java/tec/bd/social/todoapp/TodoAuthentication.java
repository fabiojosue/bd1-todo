package tec.bd.social.todoapp;

public interface TodoAuthentication {

    TodoRecord validateTodo(String sessionId, String todoId);

}
