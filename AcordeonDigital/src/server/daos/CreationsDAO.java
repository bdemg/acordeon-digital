package server.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * This class can access and modify the orders information.
 * @author (c) Copyright 2016 José A. Soto. All Rights Reserved.
 */
public class CreationsDAO extends DatabaseDAO{
    
    private static CreationsDAO creationsDAO;
    
    private final String INSERT_CREATION_QUERY = 
            "INSERT INTO creation (user_id, concept_id, creation_date) VALUES (?,?,?)";
    
    private CreationsDAO() throws SQLException{
        
        super();
    }
    
    public static CreationsDAO getCreationsDAO() throws SQLException{
        
        if(creationsDAO == null){
            creationsDAO = new CreationsDAO();
        }
        
        return creationsDAO;
    }
    
	

    public void registerNewConceptCreation(int userID, int conceptID) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.INSERT_CREATION_QUERY );
        
        queryStatement.setInt(1, userID);
        queryStatement.setInt(2 , conceptID);
        queryStatement.setTimestamp(3, new Timestamp( Calendar.getInstance().getTimeInMillis() ));
        
        queryStatement.execute();
    }
    
}
