package client.login;

import client.AcordeonController;
import client.ClientInterface;
import common.ErrorMessager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import server.ServerInterface;
import common.DAOErrorCodes;
import server.daos.UsersDAO;

/**
 *
 * @author Antonio Soto
 */
public class LoginController extends UnicastRemoteObject implements ActionListener, ClientInterface{
    
    private final LoginForm view;
    private final ServerInterface server;
    
    public LoginController( ServerInterface server ) throws RemoteException{
        
        this.view = new LoginForm();
        this.view.setLocationRelativeTo(null);
        this.view.setResizable(false);
        this.view.setVisible(true);
        
        this.server = server;
        //this.server.registerForCallback(this);
        addActionListeners();
    }
    
    private void addActionListeners(){
        
        this.view.getBtn_login().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent input_event) {
        
        Object eventSource = input_event.getSource();
        
        if( eventSource == this.view.getBtn_login() ){
            this.loginToProgram();
        }
    }
    
    private void loginToProgram(){
        
        try {
            String username = this.view.getTxtf_user().getText();
            String password = this.view.getPswdf_password().getText();
            
            int responseValue = this.server.getUserId(username, password);
            
            if( responseValue == DAOErrorCodes.USER_NOT_FOUND ){
                
                ErrorMessager.callErrorMessager().showErrorMessage( ErrorMessager.CLIENT_NOT_FOUND );
            }
            else{
                
                this.view.dispose();
                AcordeonController callbackObj = new AcordeonController(server);
            }
            
        } catch (RemoteException ex) {
            
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyEdits() throws RemoteException {
        
    }
    
}
