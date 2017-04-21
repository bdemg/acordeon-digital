/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;

/**
 *
 * @author Jorge A. Cano
 */
public class ConceptEntry implements Serializable, Comparable<ConceptEntry>{
    
    private String conceptName;
    private String category;
    private String definition;

    public ConceptEntry(String conceptName, String category, String definition) {
        this.conceptName = conceptName;
        this.category = category;
        this.definition = definition;
    }

    /**
     * @return the conceptName
     */
    public String getConceptName() {
        return conceptName;
    }

    /**
     * @param conceptName the conceptName to set
     */
    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return this.category + " - " + this.conceptName;
    }

    
    @Override
    public int compareTo(ConceptEntry conceptEntry) {
        return this.conceptName.compareTo(conceptEntry.getConceptName());
    }
    
}
