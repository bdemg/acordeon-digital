package common;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class handles the program's errors.
 * @author (c) Copyright 2016 José A. Soto. All Rights Reserved.
 */
public class ErrorMessager {
    
    private static final ErrorMessager errorMessager = new ErrorMessager();    
    private final String ERROR_TITLE = "¡Error!";
    
    // Types of errors.
    public static final String USER_NOT_FOUND = 
            "Usuario y/o contraseña inválidas.";
    public static final String NOT_A_NUMBER = 
            "No ha ingresado un valor válido.";
    public static final String EMPTY_FIELDS = 
            "Asegurese de llenar todos los campos.";
    
    private ErrorMessager(){
        ;
    }
    
    // Returns the instance of this class for use in other classes.
    public static ErrorMessager callErrorMessager(){
        
        return errorMessager;
    }
    
    // Shows the error message to the user on screen.
    public void showErrorMessage(String input_errorMessage){
        
        JFrame errorFrame = new JFrame();
        JOptionPane.showMessageDialog(
            errorFrame,
            input_errorMessage,
            this.ERROR_TITLE,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
}
