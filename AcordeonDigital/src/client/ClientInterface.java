package client;

/**
 *
 * @author Antonio Soto
 */
public interface ClientInterface extends java.rmi.Remote{
    
    public void notifyEdits() throws java.rmi.RemoteException;
}
