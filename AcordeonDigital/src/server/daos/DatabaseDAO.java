package server.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class sets up the connection to the database.
 * @author (c) Copyright 2016 José A. Soto. All Rights Reserved.
 */
public class DatabaseDAO {
    
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String HOST = "jdbc:mysql://localhost/acordeon_digital?autoReconnect=true&useSSL=false";
    private final String USER = "root";
    private final String PASSWORD = "";
    
    protected Connection connectionToDatabase = null;
    
    public DatabaseDAO() throws SQLException{
        
        try {
            Class.forName( this.DRIVER );
            this.connectionToDatabase = DriverManager.
                getConnection( this.HOST, this.USER, this.PASSWORD );

        } catch (SQLException ex) {
            throw ex;
        } catch (ClassNotFoundException ex) {
            throw new SQLException();
        }
    }
}
