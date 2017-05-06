package client.controller;

import client.controller.AcordeonController;
import client.model.ClientInterface;
import client.view.LogShit;
import common.AcordeonLogEntry;
import common.ConceptEntry;
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
    
    private final LogShit view;
    private final ServerInterface server;
    
    public LogController( ServerInterface server ) throws RemoteException{
        
        this.view = new LogShit();
        
        this.server = server;
        this.setTableProperties();
        this.addActionListeners();
        this.fillTable();
        this.view.getComboBox_Logs().addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                fillTable();
            }
        });
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
    
    private String getSelectedOption(){
        return (String)this.view.getComboBox_Logs().getSelectedItem();
    }
    
    private void fillTable(){
        AcordeonLogEntry[] allEntries;
        String selectedOption = getSelectedOption();
        try {
            switch(selectedOption){
                case "Creaciones":
                    allEntries = this.server.getCreationLogs();
                    break;
                case "Ediciones":
                    allEntries = this.server.getEditionLogs();
                    break;
                case "Eliminaciones":
                    allEntries = this.server.getEliminationLogs();
                    break;
                default:
                    allEntries = null;
                    break;
            }
            
            if( allEntries != null ){
                
                for(int i=0; i < allEntries.length; i++){
                
                    Object[] newConcept = {
                        allEntries[i].getUserName(),
                        allEntries[i].getConceptName(),
                        allEntries[i].getCategory(),
                        allEntries[i].getDate()
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
        
        if( eventSource == this.view.getBtn_Return() ){
            
            this.returnToAcordeon();
        }
    }
    
    private void returnToAcordeon(){
        
        try {
            this.view.dispose();
            AcordeonController callbackObj = new AcordeonController( this.server );
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyAcordeonChanges(ConceptEntry[] updatedConceptEntrys) throws RemoteException {
        
    }
    
    
}
