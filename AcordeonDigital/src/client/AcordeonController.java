package client;

import common.ConceptEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
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
        this.setTableProperties();
        
        this.server = server;
        //this.server.registerForCallback(this);
        this.addActionListeners();
        this.fillTable();
    }
    
    private void setTableProperties(){
        
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        this.view.getTable_Concepts().setAutoCreateRowSorter(true);
        
        this.view.getTable_Concepts().addMouseListener( new MouseAdapter(){
            
            @Override
            public void mouseClicked( MouseEvent input_event ){
                
                if( input_event.getClickCount() == 2 ){
                    
                    JTable target = (JTable) input_event.getSource();
                    int row = target.getSelectedRow();
                    System.out.println( target.getModel().getValueAt(row, 0) );
                    System.out.println( target.getModel().getValueAt(row, 1) );
                    System.out.println( target.getModel().getValueAt(row, 2) );
                    System.out.println( target.getModel().getValueAt(row, 3) );
                }
            }
        });
    }
    
    private void addActionListeners(){
        
        
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
    public void actionPerformed(ActionEvent event) {
        
        
    }

    @Override
    public void notifyEdits() throws RemoteException {
        
    }
    
}
