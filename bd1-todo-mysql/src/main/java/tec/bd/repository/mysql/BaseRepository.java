package tec.bd.repository.mysql;

import java.sql.*;

public abstract class BaseRepository<T> {

    protected final static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/todo";
    protected final static String DB_USERNAME = "root";
    protected final static String DB_PASSWORD = "admin123";


    protected Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
        return connection;
    }

    protected ResultSet query(String sqlQuery) throws SQLException {
        //Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
        var connection = connect();
        Statement stmt = connection.createStatement();
        //execute query -- consultas de seleccion
        ResultSet resultSet = stmt.executeQuery(sqlQuery);
        return resultSet;
    }

    protected ResultSet query(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    // TODO: Crear metodo execute(String sqlQuery)

    // TODO: Crear metodo execute(PreparedStatement stmt)
    protected boolean execute(PreparedStatement stmt) throws SQLException {
        return stmt.execute();
    }



    public abstract T toEntity(ResultSet resultSet);

}
