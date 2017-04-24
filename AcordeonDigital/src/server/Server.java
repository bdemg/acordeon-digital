/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.ConceptEntry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Jorge A. Cano
 */
public class Server extends UnicastRemoteObject implements SeverInterface{

    private ArrayList<Integer> conceptsBeingEdited;
    
    public Server() throws RemoteException{
        
        conceptsBeingEdited = new ArrayList<>();
    }

    @Override
    public int getUserId(String userName, String password) throws RemoteException {
        
        //llamada a un DAO para obtener la id del usuario
        //si no se encuentra la id se regresa -1 para indicar que no se encontró
        return -1;
    }

    @Override
    public void addNewConcept(ConceptEntry conceptEntry, int userId) throws RemoteException {
        
        //Se llama a un DAO para guardar el nuevo concepto
        //Se crea una entrada en el registro de creación de conceptos
    }

    @Override
    public ConceptEntry getConceptEntry(String conceptName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptEntry[] getAllConceptEntrys() throws RemoteException {
        
        //llamada a un DAO para obtener la lista de todos los conceptos
        return null;
    }

    @Override
    public boolean updateConceptDefinition(ConceptEntry updatedConceptEntry, int userId) throws RemoteException {
        
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(updatedConceptEntry.getId());
        
        //si el concepto no se está editando, se pone bajo edición y se guarda
        if(!isConceptBeingEdited){
            
            this.conceptsBeingEdited.add(updatedConceptEntry.getId());
            //llamada al DAO para actualizar el concepto
            this.conceptsBeingEdited.remove(updatedConceptEntry.getId());
            
            //llamada al DAO para registrar una modificación
            return true;
        }else{
            
            return false;
        }
    }

    @Override
    public boolean deleteConcepEntry(ConceptEntry conceptEntry, int userId) throws RemoteException {
        
        boolean isConceptBeingEdited = this.conceptsBeingEdited.contains(conceptEntry.getId());
        
        //si el concepto no se está editando, se pone bajo edición y se elimina
        if(!isConceptBeingEdited){
            
            this.conceptsBeingEdited.add(conceptEntry.getId());
            //llamada al DAO para eliminar el concepto
            this.conceptsBeingEdited.remove(conceptEntry.getId());
            return true;
        }else{
            
            return false;
        }
    }    
}
