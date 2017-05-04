package common;

/**
 *
 * @author Antonio Soto
 */
public class UserIDManager {
    
    private static final UserIDManager userIDManager = new UserIDManager();
    
    private int userID;
    
    private UserIDManager(){
        ;
    }
    
    public static UserIDManager callManager(){
        
        return userIDManager;
    }

    public int getUserID() {
        
        return userID;
    }

    public void setUserID(int userID) {
        
        this.userID = userID;
    }
}
