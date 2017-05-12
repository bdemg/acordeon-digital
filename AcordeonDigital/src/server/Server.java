/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.model.ClientInterface;
import client.model.ClientModInterface;
import com.sun.javafx.animation.TickCalculation;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.Timer;
import server.daos.EditionsDAO;
import server.daos.EliminationDAO;
import server.daos.UsersDAO;

/**
 *
 * @author Jorge A. Cano
 */
public class Server extends UnicastRemoteObject implements ServerInterface {

    private final ArrayList<Integer> conceptsBeingEdited;
    private final int TIME_TO_EDIT = 10000;

    private final int SYSTEMS_USER_ID = -69;

    private final ArrayList clientList;
    private final ArrayList clientModList;
    
    private final HashMap<ClientModInterface, Timer> timers;

    public Server() throws RemoteException {

        conceptsBeingEdited = new ArrayList<>();
        clientList = new ArrayList();
        clientModList = new ArrayList();
        timers = new HashMap<>();
    }

    @Override
    public synchronized boolean requestPermisionToModifyConcept(ClientModInterface client, int conceptID) throws RemoteException {

        if (!conceptsBeingEdited.contains(new Integer(conceptID))) {
            conceptsBeingEdited.add(new Integer(conceptID));

            setTimerToRemovePermission(client, conceptID);

            return true;
        } else {
            return false;
        }
    }

    private void setTimerToRemovePermission(ClientModInterface client, int conceptID) {

        Timer timer = new Timer(this.TIME_TO_EDIT, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    
                    doTimeoutCallbacks(conceptID);
                    relesePermisionToModifyConcept(conceptID);
                } catch (RemoteException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        this.timers.put(client, timer);
        
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public synchronized boolean relesePermisionToModifyConcept(int conceptID) throws RemoteException {

        if (conceptsBeingEdited.contains(new Integer(conceptID))) {
            conceptsBeingEdited.remove(new Integer(conceptID));
            return true;
        } else {
            return false;
        }
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

            this.doCallbacks();
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
        if (isConceptBeingEdited) {

            try {

                //llamada al DAO para actualizar el concepto
                ConceptsDAO.getConceptsDAO().updateConceptDefinition(updatedConceptEntry);

                //llamada al DAO para registrar una modificación
                EditionsDAO.getEditionsDAO().registerConceptEdition(userId, updatedConceptEntry);
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.doCallbacks();
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
            if (isConceptBeingEdited && isConceptCreator) {

                //llamada al DAO para eliminar el concepto
                ConceptsDAO.getConceptsDAO().deleteConceptEntry(conceptEntry.getId());
                //registrar el concepto que se eliminó
                EliminationDAO.getEliminationDAO().registerElimination(conceptEntry, userId);

                //Si el tema fué eliminado automaticamente, se registra también
                if (!ConceptsDAO.getConceptsDAO().doesCategoryExist(conceptEntry.getCategory())) {

                    EliminationDAO.getEliminationDAO().registerElimination(
                            new ConceptEntry(
                                    0,
                                    "",
                                    conceptEntry.getCategory(),
                                    ""
                            ),
                            this.SYSTEMS_USER_ID);
                }

                this.doCallbacks();
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

    @Override
    public void registerForAcordeonChangeCallback(ClientInterface callbackObject) throws RemoteException {

        if (!(clientList.contains(callbackObject))) {
            clientList.add(callbackObject);
            System.out.println("Registered new client ");
        }
    }

    @Override
    public void unregisterForAcordeonChangeCallback(ClientInterface callbackObject) throws RemoteException {

        if (clientList.remove(callbackObject)) {
            System.out.println("Unregistered client ");
        } else {
            System.out.println(
                    "unregister: client wasn't registered.");
        }
    }

    private void doCallbacks() throws RemoteException {

        try {
            // make callback to each registered client
            System.out.println(
                    "**************************************\n"
                    + "Callbacks initiated ---");

            ConceptEntry[] conceptEntrys = ConceptsDAO.getConceptsDAO().getAllConcepts();
            for (int i = 0; i < clientList.size(); i++) {
                System.out.println("doing " + i + "-th callback\n");
                // convert the vector object to a callback object
                ClientInterface nextClient
                        = (ClientInterface) clientList.get(i);
                // invoke the callback method
                nextClient.notifyAcordeonChanges(conceptEntrys);
            }// end for
            System.out.println("********************************\n"
                    + "Server completed callbacks ---");
        } // doCallbacks
        catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void registerForModTimeoutCallback(ClientModInterface callbackObject) throws RemoteException {

        if (!(clientModList.contains(callbackObject))) {
            clientModList.add(callbackObject);
            System.out.println("Registered new client ");
        }
    }

    @Override
    public void unregisterForModTimeoutCallback(ClientModInterface callbackObject) throws RemoteException {

        if (clientModList.remove(callbackObject)) {
            System.out.println("Unregistered client ");
            stopTimer(this.timers.get(callbackObject));
            this.timers.remove(callbackObject);
        } else {
            System.out.println(
                    "unregister: client wasn't registered.");
        }
    }

    private void stopTimer(Timer timer) {
        
        if(timer.isRunning()){
            
            timer.stop();
        }
    }
    
    private void doTimeoutCallbacks(int conceptID) throws RemoteException {

        // make callback to each registered client
        System.out.println(
                "**************************************\n"
                + "Callbacks initiated ---");

        for (int i = 0; i < clientModList.size(); i++) {
            System.out.println("doing " + i + "-th callback\n");
            // convert the vector object to a callback object
            ClientModInterface nextClient
                    = (ClientModInterface) clientModList.get(i);
            // invoke the callback method
            nextClient.notifyModTimeout(conceptID);
        }// end for
        System.out.println("********************************\n"
                + "Server completed callbacks ---");
    } // doCallbacks

}
