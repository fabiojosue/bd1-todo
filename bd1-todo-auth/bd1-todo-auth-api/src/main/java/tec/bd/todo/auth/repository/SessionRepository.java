package tec.bd.todo.auth.repository;

import tec.bd.todo.auth.ClientCredentials;
import tec.bd.todo.auth.Session;

import java.util.List;

public interface SessionRepository {

    List<Session> findAll();

    Session findBySessionId(String sessionId);

    Session save(Session session);

    Session update(Session session);
}
