/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.AcordeonLogEntry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Jorge A. Cano
 */
public class EditionsDAO extends DatabaseDAO{
    
    private static EditionsDAO editionsDAO;
    
    private final String INSERT_EDITION_QUERY = 
            "INSERT INTO editions (user_id, concept_id, edit_date) VALUES (?,?,?)";
    
    private final String GET_EDITION_ENTRYS =
            "SELECT name, concept_name, edit_date FROM users u JOIN "
            + "(SELECT e.edit_date, e.user_id, cc.concept_name FROM "
            + "editions e JOIN concepts cc ON e.concept_id=cc.concept_id) "
            + "R ON R.user_id=u.user_id";
    
    private EditionsDAO() throws SQLException{
        
        super();
    }
    
    public static EditionsDAO getEditionsDAO() throws SQLException{
        
        if(editionsDAO == null){
            editionsDAO = new EditionsDAO();
        }
        
        return editionsDAO;
    }
    
    public void registerConceptEdition(int userID, int conceptID) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.INSERT_EDITION_QUERY );
        
        queryStatement.setInt(1, userID);
        queryStatement.setInt(2 , conceptID);
        queryStatement.setTimestamp(3, new Timestamp( Calendar.getInstance().getTimeInMillis() ));
        
        queryStatement.execute();
    }
    
    public AcordeonLogEntry[] getAllEditionLogs() throws SQLException{
        
         PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_EDITION_ENTRYS );
         
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
                    results.getTimestamp("edit_date")
            );
            results.next();
        }
        
        return logEntrys;
    }
}
