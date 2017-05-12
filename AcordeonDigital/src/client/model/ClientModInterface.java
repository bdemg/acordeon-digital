package client.model;

import common.ConceptEntry;

/**
 *
 * @author Antonio Soto
 */
public interface ClientModInterface extends java.rmi.Remote{
    
    public void notifyModTimeout(int conceptID) throws java.rmi.RemoteException;
}
