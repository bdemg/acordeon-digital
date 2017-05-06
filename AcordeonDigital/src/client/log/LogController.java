package client.log;

import client.AcordeonController;
import client.ClientInterface;
import common.AcordeonLogEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JComboBox;
import server.ServerInterface;

/**
 *
 * @author Antonio Soto
 */
public class LogController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final LogSheet view;
    private final ServerInterface server;
    
    public LogController( ServerInterface server ) throws RemoteException{
        
        this.view = new LogSheet();
        
        this.server = server;
        this.setTableProperties();
        this.addActionListeners();
        this.fillTable();
    }
    
    private void setTableProperties(){
        
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        this.view.getTable_Log().setAutoCreateRowSorter(true);
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_Return().addActionListener(this);
        this.view.getComboBox_Logs().addActionListener(this);
    }
    
    private void fillTable(){
        
        try {
            AcordeonLogEntry[] allEntrys = this.server.getEliminationLogs();
            
            if( allEntrys != null ){
                
                for(int i=0; i < allEntrys.length; i++){
                
                    Object[] newConcept = {
                        allEntrys[i].getUserName(),
                        allEntrys[i].getConceptName(),
                        allEntrys[i].getCategory(),
                        allEntrys[i].getDate()
                    };
                    this.view.getLogList().addRow( newConcept );
                }
            }
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        JComboBox comboBox = (JComboBox)input_event.getSource();
        String comboValue = (String)comboBox.getSelectedItem();
        
        if( eventSource == this.view.getBtn_Return() ){
            
            this.returnToAcordeon();
        }
        else if( comboValue.equals("Creaciones") ){
            
        }
        else if( comboValue.equals("Ediciones") ){
            
        }
        else if( comboValue.equals("Eliminaciones") ){
            
        }
    }
    
    private void returnToAcordeon(){
        
        try {
            this.view.dispose();
            AcordeonController callbackObj = new AcordeonController(server);
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyEdits() throws RemoteException {
        
    }
    
    
}
