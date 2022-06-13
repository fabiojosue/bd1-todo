package tec.bd.social.todoapp;

import tec.bd.social.authentication.Session;
import tec.bd.social.authentication.SessionStatus;

public class TodoAuthenticationImpl implements  TodoAuthentication{

    private TodoResource todoResource;

    public TodoAuthenticationImpl(TodoResource todoResource){
        this.todoResource = todoResource;
    }

    @Override
    public TodoRecord validateTodo(String todoId) {
        try {
            return todoResource.validateInServer(todoId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return new TodoRecord(todoId, Status.BLOCKED);
        }
    }

}
