package common;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio Soto
 */
public class InformationMessager {
    
    private static final InformationMessager informationMessager = 
            new InformationMessager();
    private final String INFO_TITLE = "Información.";
    
    public static final String ADD_SUCCESS = 
            "Se añadió un nuevo concepto exitosamente.";
    public static final String MOD_SUCCESS = 
            "Se cambió la definición exitosamente.";
    
    private InformationMessager(){
        ;
    }
    
    public static InformationMessager callMessager(){
        return informationMessager;
    }
    
    public void showMessage(String input_message){
        
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(
            frame,
            input_message,
            this.INFO_TITLE,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
