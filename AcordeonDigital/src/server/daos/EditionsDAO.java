/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import java.sql.PreparedStatement;
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
}
