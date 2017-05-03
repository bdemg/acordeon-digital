/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.mod;

import client.AcordeonController;
import client.ClientInterface;
import common.ConceptEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.ServerInterface;

/**
 *
 * @author Antonio Soto
 */
public class ModController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final ModForm view;
    private final ServerInterface server;
    
    private ConceptEntry modConcept;
    
    public ModController( ConceptEntry modConcept, ServerInterface server ) throws RemoteException{
        
        this.view = new ModForm();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.modConcept = modConcept;
        
        this.server = server;
        this.addActionListeners();
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_ModConcept().addActionListener(this);
        this.view.getBtn_Exit().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        if( eventSource == this.view.getBtn_ModConcept() ){
            
            this.modifyConcept();
        }
        else if ( eventSource == this.view.getBtn_Exit() ) {
            
            this.returnToAcordeon();
        }
    }
    
    private void modifyConcept(){
        
        
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
