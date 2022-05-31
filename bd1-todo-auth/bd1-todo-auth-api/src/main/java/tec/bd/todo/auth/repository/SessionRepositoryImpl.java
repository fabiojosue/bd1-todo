package tec.bd.todo.auth.repository;

import tec.bd.todo.auth.ClientCredentials;
import tec.bd.todo.auth.Session;
import tec.bd.todo.auth.SessionStatus;
import tec.bd.todo.auth.datasource.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SessionRepositoryImpl extends BaseRepository<Session> implements SessionRepository {


    private static String FIND_ALL_SESSION_QUERY = "select clientid,sessionid,createdat, if(minute(timediff(utc_timestamp(), createdat)) <= 30, \"ACTIVE\", \"INACTIVE\" ) as sessionStatus from sessions order by createdat asc";
    // TODO: cambiar por procedimiento almacenado
    private static String FIND_BY_SESSION_QUERY = "select clientid,sessionid,createdat, if(minute(timediff(utc_timestamp(), createdat)) <= 30, \"ACTIVE\", \"INACTIVE\" ) as sessionStatus from sessions where sessionid = ?";
    // TODO: cambiar por procedimiento almacenado
    private static String SAVE_SESSION_QUERY = "insert into sessions(clientid, sessionid, createdat) values (?, ?, ?)";
    private static String UPDATE_SESSION_QUERY = "update sessions set sessionid = ?, createdat = ? where clientid = ?";


    public SessionRepositoryImpl(DBManager dbManager) {
        super(dbManager);
    }


    @Override
    public List<Session> findAll() {
        var sessions = new ArrayList<Session>();

        try {
            var resultSet = this.query(FIND_ALL_SESSION_QUERY);
            while(resultSet.next()) {
                var client = toEntity(resultSet);
                if(null != client) {
                    sessions.add(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessions;
    }

    public Session findBySessionId(String sessionId) {

        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_BY_SESSION_QUERY);
            statement.setString(1, sessionId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var client = toEntity(resultSet);
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Session save(Session session) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(SAVE_SESSION_QUERY);
            statement.setString(1, session.getClientId());
            statement.setString(2, session.getSessionId());
            statement.setTimestamp(3, new Timestamp(session.getCreatedAt().getTime()));

            var actual = this.execute(statement);

            // Si la session se actualizó exitosamente entonces se pone el estatus

            session.setStatus(SessionStatus.ACTIVE);

            return session;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Session update(Session session) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(UPDATE_SESSION_QUERY);
            statement.setString(1, session.getClientId());
            statement.setString(2, session.getSessionId());
            statement.setTimestamp(3, new Timestamp(session.getCreatedAt().getTime()));

            var actual = this.execute(statement);

            // Si la session se guardó exitosamente entonces se pone el estatus

            session.setStatus(SessionStatus.ACTIVE);

            return session;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Session toEntity(ResultSet resultSet) {
        try {
            var session = new Session(
                    resultSet.getString("clientid"),
                    resultSet.getString("sessionid"),
                    SessionStatus.valueOf(resultSet.getString("sessionStatus").toUpperCase()),
                    resultSet.getDate("createdat")
            );

            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
