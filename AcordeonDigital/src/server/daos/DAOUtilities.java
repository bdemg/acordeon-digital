/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.daos;

import common.AcordeonLogEntry;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jorge A. Cano
 */
public class DAOUtilities {
    
    
    protected static AcordeonLogEntry[] resultsToLogEntryArray(ResultSet results) throws SQLException {
        
        results.last();
        int totalLogCount = results.getRow();
        AcordeonLogEntry[] logEntrys = new AcordeonLogEntry[totalLogCount];
        results.first();
        
        for(int logCount = 0; logCount<totalLogCount; logCount++){
            
            logEntrys[logCount] = new AcordeonLogEntry(
                    results.getString(1),
                    results.getString(2),
                    results.getTimestamp(3)
            );
            results.next();
        }
        
        return logEntrys;
    }
}
