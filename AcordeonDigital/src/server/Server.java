/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.AcordeonLogEntry;
import common.ConceptEntry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.daos.ConceptsDAO;
import server.daos.CreationsDAO;
import common.DAOErrorCodes;
import server.daos.EditionsDAO;
import server.daos.EliminationDAO;
import server.daos.UsersDAO;

/**
 *
 * @author Jorge A. Cano
 */
public class Server extends UnicastRemoteObject implements ServerInterface {

    private ArrayList<Integer> conceptsBeingEdited;
    private final int SYSTEMS_USER_ID = -69;

    public Server() throws RemoteException {

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
            
            CreationsDAO.getCreationsDAO().registerNewConceptCreation(
                    userId, 
                    new ConceptEntry(
                            newConceptID,
                            conceptEntry.getConceptName(),
                            conceptEntry.getCategory(),
                            conceptEntry.getDefinition())
            );

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

        Integer conceptId = new Integer(updatedConceptEntry.getId());
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(conceptId);

        //si el concepto no se está editando, se pone bajo edición y se guarda
        if (!isConceptBeingEdited) {

            this.conceptsBeingEdited.add(conceptId);

            try {

                //llamada al DAO para actualizar el concepto
                ConceptsDAO.getConceptsDAO().updateConceptDefinition(updatedConceptEntry);

                //llamada al DAO para registrar una modificación
                EditionsDAO.getEditionsDAO().registerConceptEdition(userId, updatedConceptEntry);
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.conceptsBeingEdited.remove(conceptId);

            return true;
        } else {

            return false;
        }
    }

    @Override
    public boolean deleteConceptEntry(ConceptEntry conceptEntry, int userId) throws RemoteException {

        Integer conceptId = new Integer(conceptEntry.getId());
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(conceptId);

        try {

            int conceptCreatorID = CreationsDAO.getCreationsDAO().getConceptCreatorID(conceptEntry.getId());
            boolean isConceptCreator = conceptCreatorID == userId;

            //si el concepto no se está editando, se pone bajo edición y se elimina
            if (!isConceptBeingEdited && isConceptCreator) {

                this.conceptsBeingEdited.add(conceptId);
                
                //llamada al DAO para eliminar el concepto
                ConceptsDAO.getConceptsDAO().deleteConceptEntry(conceptEntry.getId());
                //registrar el concepto que se eliminó
                EliminationDAO.getEliminationDAO().registerElimination(conceptEntry, userId);
                
                //Si el tema fué eliminado automaticamente, se registra también
                if(ConceptsDAO.getConceptsDAO().doesCategoryExist(conceptEntry.getCategory())){
                    
                    EliminationDAO.getEliminationDAO().registerElimination(
                            new ConceptEntry (
                                    0,
                                    conceptEntry.getCategory(),
                                    conceptEntry.getCategory(),
                                    ""
                            ),
                            this.SYSTEMS_USER_ID);
                }
                
                this.conceptsBeingEdited.remove(conceptId);
                return true;
            } else {

                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public AcordeonLogEntry[] getCreationLogs() throws RemoteException {
        
        try {
            return CreationsDAO.getCreationsDAO().getAllCreationLogs();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public AcordeonLogEntry[] getEditionLogs() throws RemoteException {
        
        try {
            return EditionsDAO.getEditionsDAO().getAllEditionLogs();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public AcordeonLogEntry[] getEliminationLogs() throws RemoteException {
        
        try {
            return EliminationDAO.getEliminationDAO().getAllEliminationLogs();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
