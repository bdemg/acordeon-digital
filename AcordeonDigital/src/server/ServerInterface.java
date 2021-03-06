/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.model.ClientInterface;
import client.model.ClientModInterface;
import common.AcordeonLogEntry;
import common.ConceptEntry;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jorge A. Cano
 */
public interface ServerInterface extends Remote {
    
    public int getUserId(String userName, String password) throws RemoteException;
    
    public void addNewConcept(ConceptEntry conceptEntry, int userId) throws RemoteException;
    
    public ConceptEntry getConceptEntry(String conceptName) throws RemoteException;
    
    public ConceptEntry[] getAllConceptEntrys() throws RemoteException;
    
    public boolean updateConceptDefinition(ConceptEntry updatedConceptEntry, int userId) throws RemoteException;
    
    public boolean deleteConceptEntry(ConceptEntry conceptEntry, int userId) throws RemoteException;
    
    public AcordeonLogEntry[] getCreationLogs() throws RemoteException;
    
    public AcordeonLogEntry[] getEditionLogs() throws RemoteException;
    
    public AcordeonLogEntry[] getEliminationLogs() throws RemoteException;
    
    public void registerForAcordeonChangeCallback(ClientInterface callbackObject) throws RemoteException;
    
    public void unregisterForAcordeonChangeCallback(ClientInterface callbackObject) throws RemoteException;
    
    public boolean requestPermisionToModifyConcept(ClientModInterface client, int conceptID) throws RemoteException;
    
    public boolean relesePermisionToModifyConcept(int conceptID) throws RemoteException;
    
    public void registerForModTimeoutCallback(ClientModInterface callbackObject) throws RemoteException;
    
    public void unregisterForModTimeoutCallback(ClientModInterface callbackObject) throws RemoteException;
}
