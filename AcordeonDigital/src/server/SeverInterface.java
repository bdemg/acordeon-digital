/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.ConceptEntry;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jorge A. Cano
 */
public interface SeverInterface extends Remote {
    
    public int getUserId(String userName, String password) throws RemoteException;
    
    public void addNewConcept(ConceptEntry conceptEntry, int userId) throws RemoteException;
    
    public ConceptEntry getConceptEntry(String conceptName) throws RemoteException;
    
    public ConceptEntry[] getAllConceptEntrys() throws RemoteException;
    
    public boolean updateConceptDefinition(ConceptEntry updatedConceptEntry, int userId) throws RemoteException;
    
    public boolean deleteConcepEntry(ConceptEntry conceptEntry, int userId) throws RemoteException;
}
