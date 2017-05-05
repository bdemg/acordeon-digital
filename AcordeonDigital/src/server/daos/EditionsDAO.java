/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.AcordeonLogEntry;
import common.ConceptEntry;
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
            "INSERT INTO editions (user_id, concept_id, concept_name, category, edit_date) VALUES (?,?,?,?,?)";
    
    private final String GET_EDITION_ENTRYS =
            "SELECT name, concept_name, category, edit_date FROM users u JOIN editions e ON u.user_id=e.user_id";
    
    private EditionsDAO() throws SQLException{
        
        super();
    }
    
    public static EditionsDAO getEditionsDAO() throws SQLException{
        
        if(editionsDAO == null){
            editionsDAO = new EditionsDAO();
        }
        
        return editionsDAO;
    }
    
    public void registerConceptEdition(int userID, ConceptEntry conceptEntry) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.INSERT_EDITION_QUERY );
        
        queryStatement.setInt(1, userID);
        queryStatement.setInt(2 , conceptEntry.getId());
        queryStatement.setString(3, conceptEntry.getConceptName());
        queryStatement.setString(4, conceptEntry.getCategory());
        queryStatement.setTimestamp(5, new Timestamp( Calendar.getInstance().getTimeInMillis() ));
        
        queryStatement.execute();
    }
    
    public AcordeonLogEntry[] getAllEditionLogs() throws SQLException{
        
         PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_EDITION_ENTRYS );
         
         ResultSet resultSet = queryStatement.executeQuery();
         
         boolean hasResults = resultSet.last();
         
         if(hasResults){
             
             return DAOUtilities.resultsToLogEntryArray(resultSet);
         } else {
             
            return null;
        }
    }
}
