/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Jorge A. Cano
 */
public class AcordeonLogEntry implements Serializable, Comparable<AcordeonLogEntry> {
    
    private String userName;
    private String conceptName;
    private String category;
    private Timestamp date;

    public AcordeonLogEntry(String userName, String conceptName, String category, Timestamp date) {
        this.userName = userName;
        this.conceptName = conceptName;
        this.category = category;
        this.date = date;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the conceptName
     */
    public String getConceptName() {
        return conceptName;
    }

    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    @Override
    public int compareTo(AcordeonLogEntry o) {
        return this.date.compareTo(o.getDate());
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
}
