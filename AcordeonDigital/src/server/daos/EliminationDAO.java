/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.ConceptEntry;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Jorge A. Cano
 */
public class EliminationDAO extends DatabaseDAO{
    
    private static EliminationDAO eliminationDAO;
    
    private final String INSERT_ELIMINATION_QUERY = 
            "INSERT INTO eliminations (user_id, concept_name, category, elimination_date ) VALUES (?,?,?,?)";
    
    private EliminationDAO() throws SQLException{
        
        super();
    }
    
    public static EliminationDAO getEliminationDAO() throws SQLException{
        
        if(eliminationDAO == null){
            eliminationDAO = new EliminationDAO();
        }
        
        return eliminationDAO;
    }
    
    public void registerElimination(ConceptEntry conceptEntry,int userId) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.INSERT_ELIMINATION_QUERY );
        
        queryStatement.setInt(1, userId);
        queryStatement.setString(2, conceptEntry.getConceptName());
        queryStatement.setString(3, conceptEntry.getCategory());
        queryStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
        
        queryStatement.execute();
    }
}
