/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is used to ask for confirmation before doing a critical action. It holds all the confirmation messages used
 * @author Jorge A. Cano
 */
public class ConfirmationMessager {
    
    private static final ConfirmationMessager confirmationMessager = new ConfirmationMessager();
    
    public static final String CONFIRM_CONCEPT_DELETE =
        "¿Está seguro que desea eliminar este concepto?";

    private ConfirmationMessager() {
        ;
    }
    
    public static ConfirmationMessager callMessager(){
        
        return ConfirmationMessager.confirmationMessager;
    }
    
    //ask for confirmation before doing a critical action
    public boolean askForConfirmation( String input_confirmationMessage ){
        
        int answer = JOptionPane.showConfirmDialog(
            new JFrame(), 
            input_confirmationMessage, 
            null, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        return answer == JOptionPane.YES_OPTION;
    }
}
