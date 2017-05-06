package client.model;

import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Antonio Soto
 */
public class LogList extends DefaultTableModel{
    
    public static final int USER_NAME = 0;
    public static final int CONCEPT_NAME = 1;
    public static final int CONCEPT_CATEGORY = 2;
    public static final int DATE = 3;
    
    //the fields of the orders list
    private static final String[] FIELD_NAMES = { 
        "Usuario",
        "Concepto",
        "Categoría",
        "Fecha" 
    };
    
    public LogList( int input_rowCount ){
        
        super( LogList.FIELD_NAMES, input_rowCount );
    }
    
    public LogList( Object[][] input_data ){
        
        super( input_data, LogList.FIELD_NAMES );
    }
    
    //the diferent types of values that are can be written to the orders list
    @Override
    public Class<?> getColumnClass( int input_columnIndex ) {
        
        Class columnClass = String.class;
        switch ( input_columnIndex ) {
            
            case LogList.USER_NAME:
                columnClass = String.class;
                break;
                
            case LogList.CONCEPT_NAME:
                columnClass = String.class;
                break;
                
            case LogList.CONCEPT_CATEGORY:
                columnClass = String.class;
                break;
                
            case LogList.DATE:
                columnClass = Timestamp.class;
                break;
                
            default:
                break;
        }
        return columnClass;
    }
    
    @Override
    public boolean isCellEditable(int input_row, int input_column) {
        
        return false;
    }
}
