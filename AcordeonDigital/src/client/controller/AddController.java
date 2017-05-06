package client.controller;

import client.view.AddForm;
import client.controller.AcordeonController;
import client.model.ClientInterface;
import common.ConceptEntry;
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
public class AddController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final AddForm view;
    private final ServerInterface server;
    
    public AddController( ServerInterface server ) throws RemoteException{
        
        this.view = new AddForm();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.server = server;
        this.addActionListeners();
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_AddConcept().addActionListener(this);
        this.view.getBtn_Exit().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        if( eventSource == this.view.getBtn_AddConcept() ){
            
            this.addNewConcept();
        }
        else if ( eventSource == this.view.getBtn_Exit() ) {
            
            this.returnToAcordeon();
        }
    }
    
    private void addNewConcept(){
        
        if( !areFieldsEmpty() ){
            
            try {
                int userId = UserIDManager.callManager().getUserID();
                this.server.addNewConcept(this.createConceptEntry(), userId);
                
                this.resetFields();
                
                InformationMessager.callMessager().showMessage( 
                        InformationMessager.ADD_SUCCESS 
                );
                
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        else{
            ErrorMessager.callErrorMessager().showErrorMessage( 
                    ErrorMessager.EMPTY_FIELDS 
            );
        }
    }
    
    private ConceptEntry createConceptEntry(){
        
        String name = this.view.getTxtf_Name().getText();
        String category = this.view.getTxtf_Category().getText();
        String definition = this.view.getTxta_Definition().getText();
        
        ConceptEntry newConcept = new ConceptEntry(0, name, category, definition);
        return newConcept;
    }
    
    private boolean areFieldsEmpty(){
        
        return this.view.getTxtf_Name().getText().compareTo("") == 0 ||
                this.view.getTxtf_Category().getText().compareTo("") == 0 ||
                this.view.getTxta_Definition().getText().compareTo("") == 0;
    }
    
    private void resetFields(){
        
        this.view.getTxtf_Name().setText("");
        this.view.getTxtf_Category().setText("");
        this.view.getTxta_Definition().setText("");
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
