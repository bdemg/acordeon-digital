/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.ConceptEntry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jorge A. Cano
 */
public class ConceptsDAO extends DatabaseDAO{
    
    private static ConceptsDAO conceptsDAO;
    
    private final String INSERT_CONCEPT_QUERY = 
            "INSERT INTO concepts (concept_name, category, definition) VALUES (?,?,?)";
    
    private final String GET_ALL_CONCEPTS_QUERY =
            "SELECT * FROM concepts ";
    private final String GET_CONCEPT_ID_BY_NAME=
            "SELECT concept_id FROM concepts WHERE concept_name=?";
    
    private final String UPDATE_CONCEPT_DEFINITION_QUERY =
            "UPDATE concepts SET definition=? WHERE concept_id=?";
    
    private final String DELETE_CONCEPT_QUERY =
            "DELETE FROM concepts WHERE concept_id=?";
    
    
    private ConceptsDAO() throws SQLException{
        
        super();
    }
    
    
    public static ConceptsDAO getConceptsDAO() throws SQLException{
        
        if(conceptsDAO == null){
            conceptsDAO = new ConceptsDAO();
        }
        
        return conceptsDAO;
    }
    
    
    public void registerNewConcept(ConceptEntry newConcept) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.INSERT_CONCEPT_QUERY );
        
        queryStatement.setString(1, newConcept.getConceptName());
        queryStatement.setString(2, newConcept.getCategory());
        queryStatement.setString(3, newConcept.getDefinition());
        
        queryStatement.execute();
    }
    
    
    public ConceptEntry[] getAllConcepts() throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_ALL_CONCEPTS_QUERY );
        
        ResultSet results = queryStatement.executeQuery();
        
        boolean isEmptyResultSet = results.first();
        if(isEmptyResultSet){
            
            return resultsToConceptArray(results);
        } else{
            
            return null;
        }
    }

    
    private ConceptEntry[] resultsToConceptArray(ResultSet results) throws SQLException {
        
        results.last();
        int totalConceptCount = results.getRow();
        ConceptEntry[] conceptEntrys = new ConceptEntry[totalConceptCount];
        results.first();
        
        for(int conceptCount = 0; conceptCount<totalConceptCount; conceptCount++){
            
            conceptEntrys[conceptCount] = new ConceptEntry(
                    results.getInt("concept_id"),
                    results.getString("concept_name"),
                    results.getString("category"),
                    results.getString("definition")
            );
            results.next();
        }
        
        return conceptEntrys;
    }
    
    
    public int getConceptID(String conceptName) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_CONCEPT_ID_BY_NAME );
        
        queryStatement.setString(1, conceptName);
        
        ResultSet results = queryStatement.executeQuery();
        
         boolean isEmptyResultSet = results.last();
        if(isEmptyResultSet){
            
            return results.getInt("concept_id");
        } else {
            
            return DAOErrorCodes.CONCEPT_NOT_FOUND;
        }
    }
    
    
    public void updateConceptDefinition(ConceptEntry updatedConcept) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.UPDATE_CONCEPT_DEFINITION_QUERY );
        
        queryStatement.setString(1, updatedConcept.getDefinition());
        queryStatement.setInt(2, updatedConcept.getId());
        
        queryStatement.execute();
    }
    
    
    public void deleteConceptEntry(int conceptID) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.DELETE_CONCEPT_QUERY );
        
        queryStatement.setInt(1, conceptID);
        
        queryStatement.execute();
    }
}
