package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.SeverInterface;

/**
 *
 * @author Antonio Soto
 */
public class AcordeonController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final AcordeonSheet view;
    private final SeverInterface server;
    
    public AcordeonController( SeverInterface server ) throws RemoteException{
        
        this.view = new AcordeonSheet();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.server = server;
        //this.server.registerForCallback(this);
        addActionListeners();
    }
    
    private void addActionListeners(){
        
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        
    }

    @Override
    public void notifyEdits() throws RemoteException {
        
    }
    
}
