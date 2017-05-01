package client;

import common.ConceptEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerInterface;

/**
 *
 * @author Antonio Soto
 */
public class AcordeonController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final AcordeonSheet view;
    private final ServerInterface server;
    
    public AcordeonController( ServerInterface server ) throws RemoteException{
        
        this.view = new AcordeonSheet();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.server = server;
        //this.server.registerForCallback(this);
        addActionListeners();
        this.fillTable();
    }
    
    private void addActionListeners(){
        
        
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        
        
    }
    
    private void fillTable(){
        
        try {
            ConceptEntry[] allConcepts = this.server.getAllConceptEntrys();
            
            for(int i=0; i < allConcepts.length; i++){
                
                Object[] newConcept = {
                    allConcepts[i].getId(),
                    allConcepts[i].getConceptName(),
                    allConcepts[i].getCategory(),
                    allConcepts[i].getDefinition()
                };
                this.view.getAcordeonList().addRow( newConcept );
            }
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyEdits() throws RemoteException {
        
    }
    
}
