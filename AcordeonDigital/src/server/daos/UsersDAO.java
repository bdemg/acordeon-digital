/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.DAOErrorCodes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jorge A. Cano
 */
public class UsersDAO extends DatabaseDAO{
    
    private static UsersDAO usersDAO;
    
    private final String GET_USER_ID = 
            "SELECT user_id FROM users WHERE name=? AND password=?";
    
    private UsersDAO() throws SQLException{
        
        super();
    }
    
    public static UsersDAO getUsersDAO() throws SQLException{
        
        if(usersDAO == null){
            usersDAO = new UsersDAO();
        }
        
        return usersDAO;
    }
    
    public int getUserID(String userName, String password) throws SQLException{
        
        PreparedStatement queryStatement = (PreparedStatement)
                super.connectionToDatabase.prepareStatement( this.GET_USER_ID );
        
        queryStatement.setString(1, userName);
        queryStatement.setString(2, password);
        
        ResultSet resultSet = queryStatement.executeQuery();
        
        boolean wasUsernameAndPasswordCorrect = resultSet.last();
        
        if(wasUsernameAndPasswordCorrect){
            
            return resultSet.getInt("user_id");
        }
        else{
            
            return DAOErrorCodes.USER_NOT_FOUND;
        }
    }
}
