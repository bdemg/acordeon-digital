package client.controller;

import client.view.AcordeonSheet;
import client.model.ClientInterface;
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
        
        this.server = server;
        //this.server.registerForCallback(this);
        this.addActionListeners();
        this.setTableProperties();
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
                    
                    ConceptEntry concept = new ConceptEntry( 
                            (int)target.getModel().getValueAt(row, 0), 
                            (String)target.getModel().getValueAt(row, 1),
                            (String)target.getModel().getValueAt(row, 2),
                            (String)target.getModel().getValueAt(row, 3)
                    );
                    
                    showModForm(concept);
                }
            }
        });
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_Add().addActionListener(this);
        this.view.getBtn_Log().addActionListener(this);
    }
    
    private void fillTable(){
        
        try {
            ConceptEntry[] allConcepts = this.server.getAllConceptEntrys();
            
            if( allConcepts != null ){
                
                for(int i=0; i < allConcepts.length; i++){
                
                    Object[] newConcept = {
                        allConcepts[i].getId(),
                        allConcepts[i].getConceptName(),
                        allConcepts[i].getCategory(),
                        allConcepts[i].getDefinition()
                    };
                    this.view.getAcordeonList().addRow( newConcept );
                }
            }
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        if( eventSource == this.view.getBtn_Add() ){
            
            this.showAddForm();
        }
        else if( eventSource == this.view.getBtn_Log() ){
            
            this.showLogSheet();
        }
    }
    
    private void showAddForm(){
        
        try {
            this.view.dispose();
            AddController callbackObj = new AddController(server);
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }
    
    private void showModForm( ConceptEntry input_concept ){
        
        try {
            this.view.dispose();
            ModController callbackObj = new ModController( input_concept, server );
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }
    
    private void showLogSheet(){
        
        try {
            this.view.dispose();
            LogController callbackObj = new LogController( server );
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyAcordeonChanges(ConceptEntry[] updatedConceptEntrys) throws RemoteException {
        
    }
    
}
