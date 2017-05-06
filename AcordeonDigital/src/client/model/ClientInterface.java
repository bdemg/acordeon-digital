package client.model;

import common.ConceptEntry;

/**
 *
 * @author Antonio Soto
 */
public interface ClientInterface extends java.rmi.Remote{
    
    public void notifyAcordeonChanges(ConceptEntry[] updatedConceptEntrys) throws java.rmi.RemoteException;
}
