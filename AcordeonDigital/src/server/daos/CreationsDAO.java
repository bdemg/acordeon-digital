package server.daos;

import common.AcordeonLogEntry;
import common.ConceptEntry;
import common.DAOErrorCodes;
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
    
    private final String GET_CREATOR_ID_QUERY =
            "SELECT user_id FROM creation WHERE concept_id=?";
    
    private final String GET_CREATION_ENTRYS =
            "SELECT name, concept_name, creation_date FROM users u JOIN "
            + "(SELECT c.creation_date, c.user_id, cc.concept_name FROM "
            + "creation c JOIN concepts cc ON c.concept_id=cc.concept_id) "
            + "R ON R.user_id=u.user_id";
    
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
    
    public int getConceptCreatorID(int conceptID) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_CREATOR_ID_QUERY );
        
        queryStatement.setInt(1, conceptID);
        
        ResultSet resultSet = queryStatement.executeQuery();
        
        boolean wasCreatorFound = resultSet.last();
        
        if(wasCreatorFound){
            
            return resultSet.getInt("user_id");
        }
        else{
            
            return DAOErrorCodes.USER_NOT_FOUND;
        }
    }
    
    public AcordeonLogEntry[] getAllCreationLogs() throws SQLException{
        
         PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_CREATION_ENTRYS );
         
         ResultSet resultSet = queryStatement.executeQuery();
         
         boolean hasResults = resultSet.last();
         
         if(hasResults){
             
             return resultsToLogEntryArray(resultSet);
         } else {
             
            return null;
        }
    }

    private AcordeonLogEntry[] resultsToLogEntryArray(ResultSet results) throws SQLException {
        
        results.last();
        int totalLogCount = results.getRow();
        AcordeonLogEntry[] logEntrys = new AcordeonLogEntry[totalLogCount];
        results.first();
        
        for(int logCount = 0; logCount<totalLogCount; logCount++){
            
            logEntrys[logCount] = new AcordeonLogEntry(
                    results.getString("name"),
                    results.getString("concept_name"),
                    results.getTimestamp("creation_date")
            );
            results.next();
        }
        
        return logEntrys;
    }
}
