/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.ConceptEntry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.daos.ConceptsDAO;
import server.daos.CreationsDAO;
import server.daos.DAOErrorCodes;
import server.daos.EditionsDAO;
import server.daos.UsersDAO;

/**
 *
 * @author Jorge A. Cano
 */
public class Server extends UnicastRemoteObject implements SeverInterface{

    private ArrayList<Integer> conceptsBeingEdited;
    
    public Server() throws RemoteException{
        
        conceptsBeingEdited = new ArrayList<>();
    }

    @Override
    public int getUserId(String userName, String password) throws RemoteException {
        
        try {
            return UsersDAO.getUsersDAO().getUserID(userName, password);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DAOErrorCodes.USER_NOT_FOUND;
    }

    @Override
    public void addNewConcept(ConceptEntry conceptEntry, int userId) throws RemoteException {
        
        try {
            //Se llama a un DAO para guardar el nuevo concepto
            ConceptsDAO conceptsDAO = ConceptsDAO.getConceptsDAO();
            
            conceptsDAO.registerNewConcept(conceptEntry);
            
            //Se crea una entrada en el registro de creación de conceptos
            int newConceptID = conceptsDAO.getConceptID(conceptEntry.getConceptName());
            CreationsDAO.getCreationsDAO().registerNewConceptCreation(userId, newConceptID);
            
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ConceptEntry getConceptEntry(String conceptName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptEntry[] getAllConceptEntrys() throws RemoteException {
        
        try {
            //llamada a un DAO para obtener la lista de todos los conceptos
            return ConceptsDAO.getConceptsDAO().getAllConcepts();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean updateConceptDefinition(ConceptEntry updatedConceptEntry, int userId) throws RemoteException {
        
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(updatedConceptEntry.getId());
        
        //si el concepto no se está editando, se pone bajo edición y se guarda
        if(!isConceptBeingEdited){
            
            this.conceptsBeingEdited.add(updatedConceptEntry.getId());
            
            try {
                
                //llamada al DAO para actualizar el concepto
                ConceptsDAO.getConceptsDAO().updateConceptDefinition(updatedConceptEntry);
                
                //llamada al DAO para registrar una modificación
                EditionsDAO.getEditionsDAO().registerConceptEdition(userId, updatedConceptEntry.getId());
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            this.conceptsBeingEdited.remove(updatedConceptEntry.getId());
            
            return true;
        }else{
            
            return false;
        }
    }

    @Override
    public boolean deleteConcepEntry(ConceptEntry conceptEntry, int userId) throws RemoteException {
        
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(conceptEntry.getId());
        
        //si el concepto no se está editando, se pone bajo edición y se elimina
        if(!isConceptBeingEdited){
            
            this.conceptsBeingEdited.add(conceptEntry.getId());
            
            try {
                //llamada al DAO para eliminar el concepto
                ConceptsDAO.getConceptsDAO().deleteConceptEntry(conceptEntry.getId());
                
                //registrar el concepto que se eliminó
                
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            this.conceptsBeingEdited.remove(conceptEntry.getId());
            return true;
        }else{
            
            return false;
        }
    }    
}
