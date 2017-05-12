/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.controller.AcordeonController;
import client.model.ClientInterface;
import client.model.ClientModInterface;
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
public class ModController extends UnicastRemoteObject implements ActionListener, ClientModInterface{
    
    private final ModForm view;
    private final ServerInterface server;
    
    private ConceptEntry modConcept;
    private int userId;
    
    private boolean isPermitedToModify;
    
    public ModController( ConceptEntry modConcept, ServerInterface server ) throws RemoteException{
        
        this.view = new ModForm();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        this.view.getBtn_ModConcept().setEnabled(false);
        this.view.getBtn_Delete().setEnabled(false);
        
        this.modConcept = modConcept;
        this.view.getLbl_realName().setText( this.modConcept.getConceptName() );
        this.view.getLbl_realCategory().setText( this.modConcept.getCategory() );
        this.view.getTxta_Definition().setText( this.modConcept.getDefinition() );
        
        this.userId = UserIDManager.callManager().getUserID();
        this.view.setTitle("Modificar - "+userId);
        
        this.server = server;
        this.addActionListeners();
        this.setupPermisionToModify();
    }
    
    private void setupPermisionToModify(){
        
        try {
            this.isPermitedToModify = this.server.requestPermisionToModifyConcept(
                    this.modConcept.getId()
            );
            if(this.isPermitedToModify){
                this.view.getBtn_ModConcept().setEnabled(true);
                this.view.getBtn_Delete().setEnabled(true);
                //si obtiene permiso para modificar que se registre para callback de timeout
                this.server.registerForModTimeoutCallback(this);
            }
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cancelPermisionToModify(){
        
        try {
            if(this.isPermitedToModify){
                this.server.relesePermisionToModifyConcept(this.modConcept.getId());
            }
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
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
            this.cancelPermisionToModify();
            //aquí se quitaría del registro para callbacks
            this.server.unregisterForModTimeoutCallback(this);
            this.view.dispose();
            AcordeonController callbackObj = new AcordeonController(server);
        
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyModTimeout(int conceptID) throws RemoteException {
        
        //aquí se puede checar si la id del concepto que se recibe es la misma de la que se está editando
        //si es así pues se cierra la ventana
        if( this.modConcept.getId() == conceptID ){
            this.view.dispose();
            this.server.unregisterForModTimeoutCallback(this);
            AcordeonController callbackObj = new AcordeonController(server);
        }
    }

    
}
