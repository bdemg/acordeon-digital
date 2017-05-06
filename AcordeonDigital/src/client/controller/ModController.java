/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.controller.AcordeonController;
import client.model.ClientInterface;
import client.view.ModForm;
import common.ConceptEntry;
import common.ConfirmationMessager;
import common.ErrorMessager;
import common.InformationMessager;
import common.UserIDManager;
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
public class ModController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final ModForm view;
    private final ServerInterface server;
    
    private ConceptEntry modConcept;
    private int userId;
    
    public ModController( ConceptEntry modConcept, ServerInterface server ) throws RemoteException{
        
        this.view = new ModForm();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.modConcept = modConcept;
        this.view.getLbl_realName().setText( this.modConcept.getConceptName() );
        this.view.getLbl_realCategory().setText( this.modConcept.getCategory() );
        this.view.getTxta_Definition().setText( this.modConcept.getDefinition() );
        
        this.userId = UserIDManager.callManager().getUserID();
        
        this.server = server;
        this.addActionListeners();
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_ModConcept().addActionListener(this);
        this.view.getBtn_Delete().addActionListener(this);
        this.view.getBtn_Exit().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        if( eventSource == this.view.getBtn_ModConcept() ){
            
            this.modifyConceptDefinition();
        }
        else if( eventSource == this.view.getBtn_Delete() ){
            
            this.confirmDeletionOfConcept();
        }
        else if ( eventSource == this.view.getBtn_Exit() ) {
            
            this.returnToAcordeon();
        }
    }
    
    private void modifyConceptDefinition(){
        
        if( !isDefinitionFieldEmpty() ){
            
            try {
                String newDefinition = this.view.getTxta_Definition().getText();
                this.modConcept.setDefinition( newDefinition );
                
                this.server.updateConceptDefinition( this.modConcept , this.userId );
                
                InformationMessager.callMessager().showMessage( 
                        InformationMessager.MOD_SUCCESS 
                );

            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        else{
            ErrorMessager.callErrorMessager().showErrorMessage( 
                    ErrorMessager.EMPTY_FIELD 
            );
        }
    }
    
    private boolean isDefinitionFieldEmpty(){
        
        return this.view.getTxta_Definition().getText().compareTo("") == 0;
    }
    
    private void confirmDeletionOfConcept(){
        
        boolean isDeleteConfirmed = 
                ConfirmationMessager.callMessager().askForConfirmation( 
                        ConfirmationMessager.CONFIRM_CONCEPT_DELETE 
                );
        if( isDeleteConfirmed ){
            
            this.deleteConcept();
        }
    }
    
    private void deleteConcept(){
        
        try {
            boolean isOkToDelete = this.server.deleteConceptEntry( 
                    this.modConcept, this.userId 
            );
            if( isOkToDelete ){
                this.returnToAcordeon();
            }
            else{
                ErrorMessager.callErrorMessager().showErrorMessage( 
                    ErrorMessager.CANNOT_DELETE 
                );
            }
                
        } catch (RemoteException ex) {
            ex.printStackTrace();
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
    public void notifyAcordeonChanges(ConceptEntry[] updatedConceptEntrys) throws RemoteException {
        
    }
    
}
