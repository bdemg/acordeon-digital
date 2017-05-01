package client;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Antonio Soto
 */
public class AcordeonList extends DefaultTableModel{
    
    public static final int CONCEPT_ID = 0;
    public static final int CONCEPT_NAME = 1;
    public static final int CONCEPT_CATEGORY = 2;
    public static final int CONCEPT_DEFINITION = 3;
    
    //the fields of the orders list
    private static final String[] FIELD_NAMES = { "ID", "Concepto",
        "Categoría", "Definición" };
    
    public AcordeonList( int input_rowCount ){
        
        super( AcordeonList.FIELD_NAMES, input_rowCount );
    }
    
    public AcordeonList( Object[][] input_data ){
        
        super( input_data, AcordeonList.FIELD_NAMES );
    }
    
    //the diferent types of values that are can be written to the orders list
    @Override
    public Class<?> getColumnClass( int input_columnIndex ) {
        
        Class columnClass = String.class;
        switch ( input_columnIndex ) {
            
            case AcordeonList.CONCEPT_ID:
                columnClass = Integer.class;
                break;
                
            case AcordeonList.CONCEPT_NAME:
                columnClass = String.class;
                break;
                
            case AcordeonList.CONCEPT_CATEGORY:
                columnClass = String.class;
                break;
                
            case AcordeonList.CONCEPT_DEFINITION:
                columnClass = String.class;
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
